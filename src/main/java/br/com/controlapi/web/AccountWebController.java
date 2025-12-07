package br.com.controlapi.web;

import br.com.controlapi.web.openapi.AccountWebOpenApi;
import br.com.controlapi.model.entity.User;
import br.com.controlapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class AccountWebController implements AccountWebOpenApi {

    private final Logger log = LoggerFactory.getLogger(AccountWebController.class);

    private final UserService userService;

    public AccountWebController(UserService userService){
        this.userService = userService;
    }

    /**
     * GET   /Ativar : ativar do usuario.
     *
     * @param  key
     * @throws RuntimeException 404 (Not found) Chave não encontrada para ativar a conta.
     */
    @GetMapping("/ativar")
    @Override
    public String activateAccount(@RequestParam(value = "chave") String key, Model model) {
        Optional<User> user = userService.activateRegistration(key);

        if (user.isEmpty()) {
            model.addAttribute("message", "Chave não encontrada para ativar este usuário.");
            return "error";
        }

        model.addAttribute("reference", "ACT-" + System.currentTimeMillis());
        return "success";
    }
}
