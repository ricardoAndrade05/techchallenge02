package com.postech.techchallenge02.core.usecases;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.postech.techchallenge02.core.entities.ItemCardapio;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.ItemCardapioGateway;

public class ConsultaItemCardapioUseCase {
	private final ItemCardapioGateway cardapioGateway;

	private ConsultaItemCardapioUseCase(ItemCardapioGateway cardapioGateway) {
		this.cardapioGateway = cardapioGateway;
	}

	public static ConsultaItemCardapioUseCase getInstance(ItemCardapioGateway cardapioGateway) {
		return new ConsultaItemCardapioUseCase(cardapioGateway);
	}

	public ItemCardapio consultaPorId(Long id) {
		if (Objects.isNull(id)) {
			throw new DadosInvalidosException("Id inválido para consulta!");
		}
		return this.cardapioGateway.consulta(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Não há tipo de usuário com id: " + id));
	}

	public Page<ItemCardapio> consultaPorRestaurante(Long idRestaurante, Pageable paginacao) {
        if (Objects.isNull(idRestaurante)) {
            throw new DadosInvalidosException("Restaurante inválido");
        }
        final Page<ItemCardapio> itens = this.cardapioGateway.consultaPorRestaurante(idRestaurante, paginacao);
        if (itens.isEmpty()) {
            throw new RecursoNaoEncontradoException("Não há itens no cardapio para o restaurante: " + idRestaurante);
        }
        return itens;
    }

}
