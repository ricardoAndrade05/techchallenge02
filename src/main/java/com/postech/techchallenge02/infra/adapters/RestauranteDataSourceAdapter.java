package com.postech.techchallenge02.infra.adapters;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.infra.entities.Restaurante;
import com.postech.techchallenge02.infra.infra.mappers.RestauranteMapper;
import com.postech.techchallenge02.infra.repositories.RestauranteRepository;

public class RestauranteDataSourceAdapter implements RestauranteDataSource {


    private final RestauranteRepository restauranteRepository;
    private final RestauranteMapper restauranteMapper;
    
    public RestauranteDataSourceAdapter(RestauranteRepository restauranteRepository, RestauranteMapper restauranteMapper) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteMapper = restauranteMapper;
    }

    @Override
    public RestauranteDTO buscaRestaurantePorId(Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = this.restauranteRepository.findById(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            return null;
        }
        
        return restauranteMapper.toDTO(restauranteOptional.get());
    }

    @Override
    public void excluirRestaurante(Long idRestaurante) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'excluirRestaurante'");
    }

}
