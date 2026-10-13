package com.postech.techchallenge02.infra.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.postech.techchallenge02.core.dtos.AtualizarTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.infra.dtos.ErroPadraoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface TipoUsuarioAPI {

    @Operation(summary = "Recupera tipo de usuário por ID", description = "Recupera dados de um tipo de usuário específicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna dados do tipo de usuário com sucesso."),
            @ApiResponse(responseCode = "401", description = "É necessário estar logado para acessar este recurso.",
                    content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    public ResponseEntity<TipoUsuarioDTO> buscaTipoUsuarioPorId(Long idTipoUsuario);

    @Operation(summary = "Inserir tipo de usuário", description = "Insere um novo tipo de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna dados do novo tipo de usuário com sucesso."),
            @ApiResponse(responseCode = "401", description = "É necessário estar logado para acessar este recurso.",
                    content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    public ResponseEntity<?> inserirTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO);

    @Operation(summary = "Excluir tipo de usuário", description = "Exclui um tipo de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna dados do usuario logado com sucesso."),
            @ApiResponse(responseCode = "401", description = "É necessário estar logado para acessar este recurso.", 
                content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    public ResponseEntity<?> excluirTipoUsuario(ExcluirTipoUsuarioDTO excluirTipoUsuarioDTO);

    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza um tipo de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna dados do usuario logado com sucesso."),
            @ApiResponse(responseCode = "401", description = "É necessário estar logado para acessar este recurso.", 
                content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    public ResponseEntity<?> atualizarTipoUsuario(AtualizarTipoUsuarioDTO atualizarTipoUsuarioDTO);

    @Operation(summary = "Listar tipos de usuários do restaurante", description = "Recupera as informações do usuário que esta logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna dados do usuario logado com sucesso."),
            @ApiResponse(responseCode = "401", description = "É necessário estar logado para acessar este recurso.", 
                content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    public ResponseEntity<?> buscaTiposUsuarioRestaurante(Long idRestaurante, Pageable pageable);

}
