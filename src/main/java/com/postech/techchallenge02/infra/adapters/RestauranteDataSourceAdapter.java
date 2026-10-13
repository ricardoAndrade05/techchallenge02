package com.postech.techchallenge02.infra.adapters;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;

public class RestauranteDataSourceAdapter implements RestauranteDataSource {

    public RestauranteDataSourceAdapter(){

    }

    @Override
    public RestauranteDTO buscaRestaurantePorId(Long idRestaurante) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscaRestaurantePorId'");
    }

}
