package com.postech.techchallenge02.core.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.NovoItemCardapioDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;
import com.postech.techchallenge02.core.gateways.ItemCardapioGatewayImpl;
import com.postech.techchallenge02.core.gateways.RestauranteGatewayImpl;
import com.postech.techchallenge02.core.interfaces.ItemCardapioDataSource;
import com.postech.techchallenge02.core.interfaces.ItemCardapioGateway;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.presenters.ItemCardapioPresenter;
import com.postech.techchallenge02.core.usecases.CadastrarItemCardapioUseCase;
import com.postech.techchallenge02.core.usecases.ConsultaItemCardapioUseCase;

public class ItemCardapioController {

	private final ItemCardapioDataSource itemCardapioDataSource;
	private final RestauranteDataSource restauranteDataSource;

	private ItemCardapioController(ItemCardapioDataSource dataSource, RestauranteDataSource restauranteDataSource) {
		this.itemCardapioDataSource = dataSource;
		this.restauranteDataSource = restauranteDataSource;
	}

	public static ItemCardapioController getInstance(ItemCardapioDataSource dataSource,
			RestauranteDataSource restauranteDataSource) {
		return new ItemCardapioController(dataSource, restauranteDataSource);
	}

	public ItemCardapioDTO cadastro(NovoItemCardapioDTO novoItem) {
		ItemCardapioGateway cardapioGateway = ItemCardapioGatewayImpl.getInstance(itemCardapioDataSource);
		RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
		ItemCardapio itemCardapio = CadastrarItemCardapioUseCase
				.getInstance(cardapioGateway, restauranteGateway)
				.execute(novoItem);
		return ItemCardapioPresenter.toDTO(itemCardapio);
	}

	public ItemCardapioDTO consulta(Long id) {
		ItemCardapioGateway cardapioGateway = ItemCardapioGatewayImpl.getInstance(itemCardapioDataSource);
		ItemCardapio itemCardapio = ConsultaItemCardapioUseCase
				.getInstance(cardapioGateway)
				.consultaPorId(id);
		return ItemCardapioPresenter.toDTO(itemCardapio);
	}

	public Page<ItemCardapioDTO> listaPorRestaurante(Long idRestaurante, Pageable page) {
		ItemCardapioGateway cardapioGateway = ItemCardapioGatewayImpl.getInstance(itemCardapioDataSource);
		Page<ItemCardapio> lista = ConsultaItemCardapioUseCase
				.getInstance(cardapioGateway)
				.consultaPorRestaurante(idRestaurante, page);

		List<ItemCardapioDTO> pageDTO = ItemCardapioPresenter.toDTOs(lista.toList());
		Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
		return new PageImpl<>(
				pageDTO,
				pageable, pageDTO.size() / page.getPageSize());
	}
}
