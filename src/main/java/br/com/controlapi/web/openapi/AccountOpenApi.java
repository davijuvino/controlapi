package br.com.controlapi.web.openapi;

import br.com.controlapi.web.vm.ManageUserVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;

@Tag(name = "Contas")
public interface AccountOpenApi {

    @Operation(summary = "Registrar um usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail/login já em uso")
    })
    void registerAccount(
            @RequestBody(description = "Representação de um novo usuário", required = true) ManageUserVM manageVMUser);


    @Operation(summary = "Ativar um usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário ativado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao ativar o usuário")
    })
    String activateAccount(String key , Model model);

}
