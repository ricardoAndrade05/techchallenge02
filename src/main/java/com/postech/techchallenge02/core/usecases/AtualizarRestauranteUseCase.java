package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

public class AtualizarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;

    public static AtualizarRestauranteUseCase getInstance(RestauranteGateway restauranteGateway) {
        return new AtualizarRestauranteUseCase(restauranteGateway);
    }

    public Restaurante execute(Long id, Restaurante restaurante) {
        return null;
    }

    private AtualizarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

}
