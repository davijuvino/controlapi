package br.com.controlapi.web;

import br.com.controlapi.model.exception.EmailAlreadyEmUsoException;
import br.com.controlapi.model.exception.LoginAlreadyEmUsoException;
import br.com.controlapi.service.EmailService;
import br.com.controlapi.web.openapi.ContaOpenApi;
import br.com.controlapi.web.vm.GerenciarUsuarioVM;
import br.com.controlapi.model.entity.Usuario;
import br.com.controlapi.model.exception.InvalidoSenhaException;
import br.com.controlapi.repository.UsuarioRepository;
import br.com.controlapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ContaController implements ContaOpenApi {

    private final Logger log = LoggerFactory.getLogger(ContaController.class);

    private final UsuarioRepository usuarioRepository;

    private final UsuarioService usuarioService;

    private final EmailService emailService;

    public ContaController(UsuarioRepository usuarioRepository, UsuarioService usuarioService, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    /**
     * POST  /register : registrar o usuário.
     *
     * @param gerenciarUsuarioVM o modelo de exibição do usuário gerenciado
     * @throws InvalidoSenhaException 400 (Bad Request) se a senha estiver incorreta
     * @throws EmailAlreadyEmUsoException 400 (Bad Request) se o e-mail já estiver em uso
     * @throws LoginAlreadyEmUsoException 400 (Bad Request) se o login já estiver em uso
     */
    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public void registrarConta(@Valid @RequestBody GerenciarUsuarioVM gerenciarUsuarioVM) {
            if(!checkPasswordLength(gerenciarUsuarioVM.getSenha())){
                throw new InvalidoSenhaException();
            }
            Usuario usuario = usuarioService.registrarUsuario(gerenciarUsuarioVM, gerenciarUsuarioVM.getSenha());
            emailService.enviarEmailDeAtivacao(usuario);

    }


    private static boolean checkPasswordLength(String senha) {
        return StringUtils.hasText(senha) &&
                senha.length() >= GerenciarUsuarioVM.SENHA_MIN_LENGTH &&
                senha.length() <= GerenciarUsuarioVM.SENHA_MAX_LENGTH;
    }
}
