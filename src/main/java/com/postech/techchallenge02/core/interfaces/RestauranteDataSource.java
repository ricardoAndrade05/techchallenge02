package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;

public interface RestauranteDataSource {

    RestauranteDTO buscaRestaurantePorId(Long idRestaurante);
    
}
