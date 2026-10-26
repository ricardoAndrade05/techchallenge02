package com.postech.techchallenge02.core.controllers;

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
		ItemCardapioGateway cardapioGateway = ItemCardapioGatewayImpl.getInstance(itemCardapioDataSource );
		RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
		ItemCardapio itemCardapio = CadastrarItemCardapioUseCase
				.getInstance(cardapioGateway, restauranteGateway)
				.execute(novoItem);
		return ItemCardapioPresenter.toDTO(itemCardapio);
	}

}
