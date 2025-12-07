package br.com.controlapi.web.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;

@Tag(name = "Contas")
public interface AccountWebOpenApi {

    @Operation(summary = "Ativar um usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário ativado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao ativar o usuário")
    })
    String activateAccount(String key , Model model);
}
