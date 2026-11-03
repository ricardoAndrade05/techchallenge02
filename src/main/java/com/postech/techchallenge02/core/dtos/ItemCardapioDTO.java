package com.postech.techchallenge02.core.dtos;

import com.postech.techchallenge02.core.enums.DisponibilidadeCardapioEnum;

public record ItemCardapioDTO(Long id,
		String nome,
		String descricao,
		Float preco,
		DisponibilidadeCardapioEnum tipoDisponibilidade,
		String caminhoImagem,
		RestauranteDTO restaurante) {

}
