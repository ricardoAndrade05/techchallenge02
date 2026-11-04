package com.postech.techchallenge02.core.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;

public interface ItemCardapioDataSource {
  
	ItemCardapioDTO salva(ItemCardapio itemCardapioDomain);

	Optional<ItemCardapio> consulta(Long id);

	Page<ItemCardapio> listaItensRestaurante(Long id, Pageable pageable);

    void excluiItem(Long id);
}
