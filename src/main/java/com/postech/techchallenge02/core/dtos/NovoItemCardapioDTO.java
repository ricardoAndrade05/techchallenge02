package com.postech.techchallenge02.core.dtos;

public record NovoItemCardapioDTO(
		Long idRestaurante,
		String nome,
		String descricao,
		Float preco,
		Integer tipoDisponibilidade,
		String caminhoImagem) {

}
