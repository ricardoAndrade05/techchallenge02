package com.postech.techchallenge02.core.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.postech.techchallenge02.core.dtos.NovoItemCardapioDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.enums.DisponibilidadeCardapioEnum;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.ItemCardapioGateway;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

public class CadastrarItemCardapioUseCase {

	private RestauranteGateway restauranteGateway;
	private ItemCardapioGateway cardapioGateway;

	private final DisponibilidadeCardapioEnum disponibilidadePadrao = DisponibilidadeCardapioEnum.TODOS;

	private CadastrarItemCardapioUseCase(ItemCardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
		this.cardapioGateway = cardapioGateway;
		this.restauranteGateway = restauranteGateway;
	}

	public static CadastrarItemCardapioUseCase getInstance(ItemCardapioGateway cardapioGateway,
			RestauranteGateway restauranteGateway) {
		return new CadastrarItemCardapioUseCase(cardapioGateway, restauranteGateway);
	}

	public ItemCardapio execute(NovoItemCardapioDTO novoItem) {
		this.validaDadosEntrada(novoItem);
		var tipoDisponibilidade = this.obtemTipoDisponibilidade(novoItem);
		
		Restaurante restaurante = this.restauranteGateway.buscaRestaurantePorId(novoItem.idRestaurante())
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Restaurante " + novoItem.idRestaurante() + " não encontrado"));

		ItemCardapio item = ItemCardapio.cria(novoItem.nome(), novoItem.descricao(), novoItem.preco(),
				tipoDisponibilidade, novoItem.caminhoImagem(), restaurante);
		return this.cardapioGateway.salva(item);
	}

	private void validaDadosEntrada(NovoItemCardapioDTO dto) {
		List<String> erros = new ArrayList<>();

		if (Objects.isNull(dto.idRestaurante())) {
			erros.add("Restaurante inválido!");
		}
		if (Objects.isNull(dto.nome())) {
			erros.add("Nome do item do cardápio inválido!");
		}
		if (Objects.isNull(dto.preco()) || dto.preco() <= 0) {
			erros.add("Preço inválido!");
		}
		if (!erros.isEmpty()) {
			throw new DadosInvalidosException(String.join(" ", erros));
		}
	}

	private DisponibilidadeCardapioEnum obtemTipoDisponibilidade(NovoItemCardapioDTO novoItem) {
		var codigoDisponibilidade = Objects.isNull(novoItem.tipoDisponibilidade())
				? disponibilidadePadrao.getCodigo()
				: novoItem.tipoDisponibilidade();
		return DisponibilidadeCardapioEnum.fromCodigo(codigoDisponibilidade);
	}
}
