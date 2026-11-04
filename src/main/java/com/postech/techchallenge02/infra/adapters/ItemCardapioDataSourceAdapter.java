package com.postech.techchallenge02.infra.adapters;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;
import com.postech.techchallenge02.core.interfaces.ItemCardapioDataSource;
import com.postech.techchallenge02.infra.infra.mappers.ItemCardapioMapper;
import com.postech.techchallenge02.infra.jpa.entities.ItemCardapioEntity;
import com.postech.techchallenge02.infra.jpa.entities.RestauranteEntity;
import com.postech.techchallenge02.infra.jpa.repositories.ItemCardapioRepository;
import com.postech.techchallenge02.infra.jpa.repositories.RestauranteRepository;

@Component
public class ItemCardapioDataSourceAdapter implements ItemCardapioDataSource {
	private final ItemCardapioRepository cardapioRepo;
	private final RestauranteRepository restauranteRepo;
	private final ItemCardapioMapper cardapioMapper;

	public ItemCardapioDataSourceAdapter(ItemCardapioRepository cardapioRepository,
			RestauranteRepository restauranteRepo, ItemCardapioMapper cardapioMapper) {
		this.cardapioRepo = cardapioRepository;
		this.restauranteRepo = restauranteRepo;
		this.cardapioMapper = cardapioMapper;
	}

	@Override
	public ItemCardapioDTO salva(ItemCardapio itemCardapioDomain) {
		RestauranteEntity restauranteDB = this.restauranteRepo.findById(itemCardapioDomain.getIdRestaurante()).get();
		ItemCardapioEntity itemCardapioDB = this.cardapioMapper
				.toEntity(itemCardapioDomain);
		itemCardapioDB.setRestaurante(restauranteDB);
		itemCardapioDB = this.cardapioRepo.saveAndFlush(itemCardapioDB);
		return cardapioMapper.toDTO(itemCardapioDB);
	}

	@Override
	public Optional<ItemCardapio> consulta(Long id) {
		var itemCardapioDB = this.cardapioRepo.findById(id);
		if (itemCardapioDB.isPresent()) {
			return Optional.of(this.cardapioMapper.toDomain(itemCardapioDB.get()));
		}
		return Optional.empty();
	}

	public Page<ItemCardapio> listaItensRestaurante(Long id, Pageable pageable) {
		Page<ItemCardapioEntity> lista = this.cardapioRepo.findByRestauranteId(id, pageable);
		return lista.map(this.cardapioMapper::toDomain);
	}

	@Override
	public void excluiItem(Long id) {
		this.cardapioRepo.deleteById(id);
	}
}

