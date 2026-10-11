package com.postech.techchallenge02.core.interfaces;

import java.util.Optional;

import com.postech.techchallenge02.core.entities.Restaurante;

public interface RestauranteGateway {

    Optional<Restaurante> buscaRestaurantePorId(Long id);

}
