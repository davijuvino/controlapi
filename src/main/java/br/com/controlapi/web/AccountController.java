package br.com.controlapi.web;

import br.com.controlapi.model.exception.*;
import br.com.controlapi.service.EmailService;
import br.com.controlapi.web.openapi.AccountOpenApi;
import br.com.controlapi.web.vm.ManageUserVM;
import br.com.controlapi.model.entity.User;
import br.com.controlapi.repository.UserRepository;
import br.com.controlapi.service.UserService;
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
     * @throws EmailAlreadyInUseException 400 (Bad Request) se o e-mail já estiver em uso
     * @throws LoginAlreadyInUseException 400 (Bad Request) se o login já estiver em uso
     */
    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public void registerAccount(@Valid @RequestBody ManageUserVM manageVMUser) {
            if(!checkPasswordLength(manageVMUser.getSenha())){
                throw new InvalidPasswordException();
            }
            User user = userService.registerUser(manageVMUser, manageVMUser.getSenha());
            emailService.enviarEmailDeAtivacao(user);

    }

    /**
     * GET   /Ativar : ativar do usuario.
     * @param key
     * @throws RuntimeException 500 (Internal Server Error) se o Usuaro não estiver ativado.
     */
    @GetMapping("/ativar")
    @Override
    public void activateAccount(@RequestParam(value = "chave") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Chave não encontrado para ativar este Usuario.");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return StringUtils.hasText(password) &&
                password.length() >= ManageUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManageUserVM.PASSWORD_MAX_LENGTH;
    }
}
