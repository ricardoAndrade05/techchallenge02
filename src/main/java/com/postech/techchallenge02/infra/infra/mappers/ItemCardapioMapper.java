package com.postech.techchallenge02.infra.infra.mappers;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;
import com.postech.techchallenge02.infra.jpa.entities.ItemCardapioEntity;

@Component
public class ItemCardapioMapper {

	private final RestauranteMapper restauranteMapper;

	public ItemCardapioMapper(RestauranteMapper restauranteMapper) {
		this.restauranteMapper = restauranteMapper;
	}

	public ItemCardapioDTO toDTO(ItemCardapioEntity entity) {
		if (entity == null) {
			return null;
		}
		RestauranteDTO restaurante = restauranteMapper.toDTO(entity.getRestaurante());
		ItemCardapioDTO itemCardapioDTO = new ItemCardapioDTO(
				entity.getId(), entity.getNome(), entity.getDescricao(), entity.getPreco(),
				entity.getTipoDisponibilidade(), entity.getCaminhoImagem(), restaurante);

		return itemCardapioDTO;
	}

	public ItemCardapio toDomain(ItemCardapioEntity entity) {
		if (entity == null) {
			return null;
		}
		ItemCardapio itemCardapio = ItemCardapio.restaura(
				entity.getId(), entity.getNome(), entity.getDescricao(), entity.getPreco(),
				entity.getTipoDisponibilidade(), entity.getCaminhoImagem(), null);

		return itemCardapio;
	}
	
	public ItemCardapioEntity toEntity(ItemCardapio itemCardapioDomain) {
		if (itemCardapioDomain == null) {
			return null;
		}

		ItemCardapioEntity entity = new ItemCardapioEntity();

		entity.setNome(itemCardapioDomain.getNome());
		entity.setDescricao(itemCardapioDomain.getDescricao());
		entity.setPreco(itemCardapioDomain.getPreco());
		entity.setCaminhoImagem(itemCardapioDomain.getCaminhoImagem());
		entity.setTipoDisponibilidade(itemCardapioDomain.getTipoDisponibilidade());
		return entity;
	}
}
