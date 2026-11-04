package com.postech.techchallenge02.infra.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.NovoItemCardapioDTO;
import com.postech.techchallenge02.infra.dtos.ErroPadraoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

interface ItemCardapioAPI {

	@Operation(summary = "Recupera item do cardapio por ID", description = "Recupera dados de um item de cardapio específico.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna dados do item de cardapio com sucesso."),
			@ApiResponse(responseCode = "404", description = "Item de cardapio não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
	})
	ResponseEntity<ItemCardapioDTO> consultaPorId(@PathVariable Long id);

	@Operation(summary = "Lista os itens de cardapio do restaurante", description = "Recupera as informações dos itens de um restaurante paginadas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Retorna dados dos itens do restaurante com sucesso."),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado ou não há itens cadastrados.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
	})
	ResponseEntity<Page<ItemCardapioDTO>> listaItensRestaurante(@PathVariable Long idRestaurante,
			Pageable pageable);

	@Operation(summary = "Insere item no cardapio", description = "Insere um novo item de cardapio")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Item de cardápio criado com sucesso."),
			@ApiResponse(responseCode = "409", description = "Item de cardápio já cadastrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class))),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
	})
	ResponseEntity<ItemCardapioDTO> insereItem(@RequestBody NovoItemCardapioDTO itemCardapioDTO);

	@Operation(summary = "Exclui item do cardapio", description = "Exclui um item do cardapio.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Exclusão de item realizada com sucesso."),
			@ApiResponse(responseCode = "404", description = "Item do cardápio não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
	})
	ResponseEntity<Void> exclui(@PathVariable Long id, @RequestHeader("idUsuario") Long idUsuario);

	@Operation(summary = "Atualiza item do cardapio", description = "Atualiza um item do cardapio.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Item atualizado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Item de cardapio não encontrado.", content = @Content(schema = @Schema(implementation = ErroPadraoDTO.class)))
	})
	ResponseEntity<ItemCardapioDTO> atualiza(@PathVariable Long id,
			@RequestBody ItemCardapioDTO itemCardapioDTO);
}
