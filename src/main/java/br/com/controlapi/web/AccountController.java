package br.com.controlapi.web;

import br.com.controlapi.model.dto.PasswordChangeDTO;
import br.com.controlapi.model.dto.UserDTO;
import br.com.controlapi.model.exception.*;
import br.com.controlapi.security.SecurityUtils;
import br.com.controlapi.service.EmailService;
import br.com.controlapi.web.openapi.AccountOpenApi;
import br.com.controlapi.web.vm.KeyAndPasswordVM;
import br.com.controlapi.web.vm.ManagedUserVM;
import br.com.controlapi.model.entity.User;
import br.com.controlapi.repository.UserRepository;
import br.com.controlapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController implements AccountOpenApi {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final EmailService emailService;

    public AccountController(UserRepository userRepository, UserService userService, EmailService emailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    /**
     * POST  /registrar : registrar o usuário.
     *
     * @param manageVMUser o modelo de exibição do usuário gerenciado
     * @throws InvalidPasswordException 400 (Bad Request) se a senha estiver incorreta
     * @throws EmailAlreadyUsedException 400 (Bad Request) se o e-mail já estiver em uso
     * @throws LoginAlreadyInUseException 400 (Bad Request) se o login já estiver em uso
     */
    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public void registerAccount(@Valid @RequestBody ManagedUserVM manageVMUser) {
        if(!checkPasswordLength(manageVMUser.getSenha())){
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(manageVMUser, manageVMUser.getSenha());
        emailService.sendActivationEmail(user);

    }

    /**
     * GET  /autenticado : Verifique se o usuário está autenticado e retorne suas credenciais de login.
     *
     * @param request the HTTP request
     * @return o login se o usuario estiver autenticado
     */
    @GetMapping("/autenticado")
    @Override
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST requisicao para verificar se o usuario atual esta autenticado");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : recupera usuario atual.
     *
     * @return usuario atual
     * @throws RuntimeException 404 (Not found) retorna se o usuario não for encontrado.
     */
    @GetMapping("/conta")
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
                .map(UserDTO::new)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    /**
     * POST  /conta : atualizar informações atuais do usuario.
     *
     * @param userDTO o usuario a ser atualizado
     * @throws EmailAlreadyUsedException 400 (Bad Request) se o e-mail já estiver em uso
     * @throws RuntimeException 404 (Internal Server Error) se o usuario não for encontrado
     */
    @PostMapping("/conta")
    @Override
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UserNotFoundException("Login não encontrado para usuario atual"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }
        userService.updateUser(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                userDTO.getLangKey()
        );
    }

    /**
     * POST  /conta/troca-senha : trocar a senha do usuario atual.
     *
     * @param passwordChangeDto nova senha atual
     * @throws InvalidPasswordException 400 (Bad Request) se a nova senha esta incorreta;
     */
    @PostMapping(path = "/conta/troca-senha")
    @Override
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * POST   /conta/reseta-senha/inicia : Envie um e-mail para redefinir a senha do usuário.
     *
     * @param mail email para usuario.
     * @throws EmailNotFoundException 400 (Bad Request) se o endereço de email não esta registrado.
     */
    @PostMapping(path = "/conta/reseta-senha/inicia")
    @Override
    public void requestPasswordReset(@RequestBody String mail) {
        emailService.sendPasswordResetMail(
                userService.requestPasswordReset(mail)
                        .orElseThrow(EmailNotFoundException::new)
        );
    }

    /**
     * POST   /conta/reseta-senha/finaliza : Finalize a redefinição da senha do usuário.
     *
     * @param keyAndPassword a chave gerada e a nova senha
     * @throws InvalidPasswordException 400 (Bad Request) se a senha estiver incorreta
     * @throws RuntimeException 404 (Notfound) se a senha não puder ser redefinida
     */
    @PostMapping(path = "/account/reseta-senha/finaliza")
    @Override
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user =
                userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (user.isEmpty()) {
            throw new UserNotFoundException("Nenhum usuário foi encontrado para esta chave de redefinição.");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return StringUtils.hasText(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}