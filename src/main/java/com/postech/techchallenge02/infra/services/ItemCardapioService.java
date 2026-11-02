package com.postech.techchallenge02.infra.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.techchallenge02.core.controllers.ItemCardapioController;
import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.NovoItemCardapioDTO;
import com.postech.techchallenge02.core.interfaces.ItemCardapioDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;


@Service
public class ItemCardapioService {
    
    private final ItemCardapioController itemCardapioController;

    public ItemCardapioService(ItemCardapioDataSource dataSource, RestauranteDataSource restauranteDataSource) {
        this.itemCardapioController = ItemCardapioController.getInstance(dataSource, restauranteDataSource);
    }

    @Transactional
    public ItemCardapioDTO cadastra(NovoItemCardapioDTO novoItemCardapioDTO) {
        return this.itemCardapioController.cadastro(novoItemCardapioDTO);
    }

    @Transactional(readOnly = true)
    public ItemCardapioDTO buscarPorId(Long id) {
        return this.itemCardapioController.consulta(id);
    }

    
    @Transactional(readOnly = true)
    public Page<ItemCardapioDTO> listaItensRestaurante(Long idRestaurante, Pageable pageable) {
        return this.itemCardapioController.listaPorRestaurante(idRestaurante, pageable);
    }

    @Transactional
    public ItemCardapioDTO atualizaItemCardapio(Long id, ItemCardapioDTO dto) {
        return null; //this.itemCardapioController.atualizarRestaurante(atualizarItemCardapioDTO);
    }

    @Transactional
    public void exclui(Long idRestaurante) {
        //this.restauranteController.excluirRestaurante(new ExcluirItemCardapioDTO(idUsuarioLogado, idRestaurante));
    }

    @Transactional
    public ItemCardapioDTO cadastra(ItemCardapioDTO novoItemCardapioDTO) {
        return null; //this.restauranteController.cadastrarRestaurante(novoItemCardapioDTO);
    }
}
