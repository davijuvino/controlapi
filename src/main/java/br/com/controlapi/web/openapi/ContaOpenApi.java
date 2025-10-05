package br.com.controlapi.web.openapi;

import br.com.controlapi.web.vm.GerenciarUsuarioVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Contas")
public interface ContaOpenApi {

    @Operation(summary = "Registrar um usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail/login já em uso")
    })
    void registrarConta(
            @RequestBody(description = "Representação de um novo usuário", required = true) GerenciarUsuarioVM gerenciarUsuarioVM);
}
