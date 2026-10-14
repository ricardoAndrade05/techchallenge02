package com.postech.techchallenge02.infra.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.infra.dtos.AtualizarTipoDTO;
import com.postech.techchallenge02.infra.dtos.ErroPadraoDTO;
import com.postech.techchallenge02.infra.dtos.ExcluiTipoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface TipoUsuarioAPI {

    @Operation(summary = "Recupera tipo de usuário por ID", description = "Recupera dados de um tipo de usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna dados do tipo de usuário com sucesso."),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<TipoUsuarioDTO> buscaTipoUsuarioPorId(@PathVariable Long idTipoUsuario);

    @Operation(summary = "Listar tipos de usuários do restaurante", description = "Recupera as informações dos tipos de usuário de um restaurante paginadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna dados dos tipos de usuário do restaurante com sucesso."),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado ou não há tipos de usuários cadastrados.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<Page<TipoUsuarioDTO>> buscaTiposUsuarioRestaurante(@PathVariable Long idRestaurante, Pageable pageable);

    @Operation(summary = "Inserir tipo de usuário", description = "Insere um novo tipo de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de usuário criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Tipo de usuário já cadastrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou restaurante não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<TipoUsuarioDTO> inserirTipoUsuario(@RequestBody NovoTipoUsuarioDTO novoTipoUsuarioDTO);

    @Operation(summary = "Excluir tipo de usuário", description = "Exclui um tipo de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exclusão de tipo de usuário realizada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou tipo de usuário não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "409", description = "O tipo de usuário está vinculado a um ou mais usuários.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<Void> excluirTipoUsuario(@PathVariable Long id, @RequestBody ExcluiTipoDTO excluiTipoDTO);

    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza um tipo de usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou tipo de usuário não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "409", description = "Tipo de usuário já existente.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<TipoUsuarioDTO> atualizarTipoUsuario(@PathVariable Long id, @RequestBody AtualizarTipoDTO atualizarTipoDTO);
}
