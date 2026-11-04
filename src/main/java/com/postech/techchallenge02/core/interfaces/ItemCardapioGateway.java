package com.postech.techchallenge02.core.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.postech.techchallenge02.core.entities.ItemCardapio;

public interface ItemCardapioGateway {

    ItemCardapio salva(ItemCardapio item);

    Optional<ItemCardapio> consulta(Long id);

    Page<ItemCardapio> consultaPorRestaurante(Long id, Pageable pageable);

    void exclusaoItem(Long id);
}
