package com.postech.techchallenge02.infra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.infra.dtos.AtualizaRestauranteDTO;
import com.postech.techchallenge02.infra.dtos.ErroPadraoDTO;
import com.postech.techchallenge02.infra.dtos.ExcluiRestauranteDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface RestauranteAPI {

    @Operation(summary = "Recupera restaurante por ID", description = "Recupera os dados de um restaurante específico pelo seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os dados do restaurante com sucesso."),
            @ApiResponse(responseCode = "400", description = "Identificador do restaurante inválido ou nulo.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado para o ID informado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<RestauranteDTO> buscarPorId(@PathVariable Long id);

    @Operation(summary = "Inserir restaurante", description = "Cadastra um novo restaurante e vincula o usuário logado como proprietário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados do restaurante ou endereço inválidos.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário logado não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<RestauranteDTO> inserirRestaurante(@RequestBody NovoRestauranteDTO dto);

    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente e processa a troca de proprietário, se informada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados para atualização inválidos ou em formato incorreto.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para atualizar o restaurante.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante, usuário logado ou novo proprietário não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<RestauranteDTO> atualizarRestaurante(@PathVariable Long id, @RequestBody AtualizaRestauranteDTO dto);

    @Operation(summary = "Excluir restaurante", description = "Exclui um restaurante, seus tipos de usuário associados e desvincula os usuários vinculados a ele.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exclusão do restaurante realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados informados para exclusão são inválidos.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para excluir o restaurante.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário logado não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
    })
    ResponseEntity<Void> excluirRestaurante(@PathVariable Long id, @RequestBody ExcluiRestauranteDTO dto);
}
