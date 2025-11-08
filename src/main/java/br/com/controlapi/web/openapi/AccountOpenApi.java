package br.com.controlapi.web.openapi;

import br.com.controlapi.model.dto.PasswordChangeDTO;
import br.com.controlapi.model.dto.UserDTO;
import br.com.controlapi.web.vm.KeyAndPasswordVM;
import br.com.controlapi.web.vm.ManageUserVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "Verificar se o usuário está autenticado", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    String isAuthenticated(HttpServletRequest request);

    @Operation(summary = "Obter a conta do usuário autenticado", responses = {
            @ApiResponse(responseCode = "200", description = "Conta do usuário obtida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    UserDTO getAccount();

    @Operation(summary = "Salvar ou atualizar uma conta de usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Conta salva ou atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    void saveAccount(@RequestBody(description = "Representação do usuário a ser salvo", required = true) UserDTO userDTO);

    @Operation(summary = "Alterar a senha do usuário autenticado", responses = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Senha inválida")
    })
    void changePassword(@RequestBody(description = "Senha atual e nova senha", required = true) PasswordChangeDTO passwordChangeDTO);

    @Operation(summary = "Solicitar redefinição de senha", responses = {
            @ApiResponse(responseCode = "200", description = "Solicitação de redefinição de senha enviada com sucesso"),
            @ApiResponse(responseCode = "400", description = "E-mail inválido")
    })
    void requestPasswordReset(@RequestBody(description = "E-mail do usuário para resetar a senha", required = true) String email);

    @Operation(summary = "Finalizar redefinição de senha", responses = {
            @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para redefinição de senha")
    })
    void finishPasswordReset(@RequestBody(description = "Nova chave e nova senha redefinida do usuario", required = true) KeyAndPasswordVM keyAndPasswordVM);


}
