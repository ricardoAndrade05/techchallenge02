package com.postech.techchallenge02.infra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.postech.techchallenge02.core.dtos.AssociarUsuarioAoTipoDTO;
import com.postech.techchallenge02.core.dtos.DesassociarUsuarioTipoDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.infra.dtos.ErroPadraoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface UsuarioAPI {

    @Operation(summary = "Recupera tipo de usuário por ID", description = "Recupera dados de um tipo de usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário com o tipo de usuário associado com sucesso."),
            @ApiResponse(responseCode = "403", description = "O tipo usuario não pertence ao restaurante do usuario logado ou Usuário não tem permissão para realizar esta ação.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado, Tipo de usuario não encontrado ou Usuario a ser vinculado não encontrado ", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "409", description = "Usuario já possui um tipo de usuario cadastrado para este restaurante.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<UsuarioDTO> associarUsuarioAoTipo(@RequestBody AssociarUsuarioAoTipoDTO dto);

    @Operation(summary = "Excluir tipo de usuário", description = "Exclui um tipo de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exclusão de tipo de usuário realizada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para realizar esta ação.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou Usuario não possui tipo com o restaurante para ser desassociado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
    })
    ResponseEntity<Void> desassociarUsuarioAoTipoUsuario(@RequestBody DesassociarUsuarioTipoDTO dto);

}
