package com.postech.techchallenge02.infra.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.NovoItemCardapioDTO;
import com.postech.techchallenge02.infra.services.ItemCardapioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itens-cardapio")
@Tag(name = "Itens do Cardápio", description = "Endpoints para gerenciamento dos itens do cardápio")
public class ItemCardapioApiController implements ItemCardapioAPI {

	private final ItemCardapioService itemCardapioService;

	ItemCardapioApiController(ItemCardapioService itemCardapioService) {
		this.itemCardapioService = itemCardapioService;
	}

	@GetMapping("/{id}")
	@Override
	public ResponseEntity<ItemCardapioDTO> consultaPorId(Long id) {
		var dto = this.itemCardapioService.buscarPorId(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/restaurante/{idRestaurante}")
	public ResponseEntity<Page<ItemCardapioDTO>> listaItensRestaurante(@PathVariable Long idRestaurante,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<ItemCardapioDTO> itens = this.itemCardapioService.listaItensRestaurante(idRestaurante, pageable);
		return ResponseEntity.ok().body(itens);
	}

	@Override
	@PostMapping
	public ResponseEntity<ItemCardapioDTO> insereItem(NovoItemCardapioDTO novoItem) {
		var itemCardapioCadastrado = this.itemCardapioService.cadastra(novoItem);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemCardapioCadastrado);
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> exclui(@PathVariable Long id, @RequestHeader("idUsuario") Long idUsuario) {
		this.itemCardapioService.exclui(id, idUsuario);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PutMapping("/{id}")
	public ResponseEntity<ItemCardapioDTO> atualiza(Long id, ItemCardapioDTO itemCardapioDTO) {
		var itemCardapioAtualizado = this.itemCardapioService.atualizaItemCardapio(id, itemCardapioDTO);
		return ResponseEntity.ok(itemCardapioAtualizado);
	}

}
