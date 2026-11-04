package com.postech.techchallenge02.infra.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.techchallenge02.core.controllers.ItemCardapioController;
import com.postech.techchallenge02.core.dtos.ExclusaoItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.dtos.NovoItemCardapioDTO;
import com.postech.techchallenge02.core.interfaces.ItemCardapioDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;


@Service
public class ItemCardapioService {
    
    private final ItemCardapioController itemCardapioController;

    public ItemCardapioService(ItemCardapioDataSource dataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource, TipoUsuarioDataSource tipoUsuarioDataSource) {
        this.itemCardapioController = ItemCardapioController.getInstance(dataSource, restauranteDataSource, usuarioDataSource, tipoUsuarioDataSource);
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
        return null; 
    }

    @Transactional
    public void exclui(Long id, Long idUsuario ) {
        this.itemCardapioController.excluiItemCardapio(new ExclusaoItemCardapioDTO(idUsuario, id));
    }

}
