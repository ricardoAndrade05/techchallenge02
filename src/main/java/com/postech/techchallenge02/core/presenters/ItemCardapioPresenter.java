package com.postech.techchallenge02.core.presenters;

import java.util.List;
import java.util.stream.Collectors;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;

public class ItemCardapioPresenter {
	public static ItemCardapioDTO toDTO(ItemCardapio item) {
		RestauranteDTO restauranteDTO = new RestauranteDTO(item.getIdRestaurante(),
				item.getRestaurante().getNome(), null, null, null, null, null);
		return new ItemCardapioDTO(item.getId(), item.getNome(), item.getDescricao(), item.getPreco(),
				item.getTipoDisponibilidade(), item.getCaminhoImagem(), restauranteDTO);
	}

	public static List<ItemCardapioDTO> toDTOs(List<ItemCardapio> itens) {
		return itens.stream().map(ItemCardapioPresenter::toDTO).collect(Collectors.toList());
	}
}
