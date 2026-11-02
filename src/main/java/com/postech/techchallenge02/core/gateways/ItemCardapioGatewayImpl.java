package com.postech.techchallenge02.core.gateways;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.postech.techchallenge02.core.entities.ItemCardapio;
import com.postech.techchallenge02.core.interfaces.ItemCardapioDataSource;
import com.postech.techchallenge02.core.interfaces.ItemCardapioGateway;

public class ItemCardapioGatewayImpl implements ItemCardapioGateway {

	private final ItemCardapioDataSource dataSource;

	private ItemCardapioGatewayImpl(ItemCardapioDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static ItemCardapioGatewayImpl getInstance(ItemCardapioDataSource dataSource) {
		if (dataSource == null) {
			throw new IllegalArgumentException("O data source não pode ser nulo");
		}
		return new ItemCardapioGatewayImpl(dataSource);
	}

	@Override
	public ItemCardapio salva(ItemCardapio item) {
		var dto = this.dataSource.salva(item);

		return ItemCardapio.restaura(dto.id(),
				dto.nome(), dto.descricao(), dto.preco(), dto.tipoDisponibilidade(), dto.caminhoImagem(),
				item.getRestaurante());
	}

	@Override
	public Optional<ItemCardapio> consulta(Long id) {
		return this.dataSource.consulta(id);
	}

	@Override
	public Page<ItemCardapio> consultaPorRestaurante(Long idRestaurante, Pageable pageable) {
		return this.dataSource.listaItensRestaurante(idRestaurante, pageable);
	}

}
