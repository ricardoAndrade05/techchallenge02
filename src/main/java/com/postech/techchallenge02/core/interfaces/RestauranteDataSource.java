package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;

public interface RestauranteDataSource {

    RestauranteDTO buscaRestaurantePorId(Long idRestaurante);

    void excluirRestaurante(Long idRestaurante);

    RestauranteDTO cadastrarRestaurante(NovoRestauranteDTO novoRestauranteDTO);

    RestauranteDTO atualizarRestaurante(AtualizarRestauranteDTO dto);
    
}
