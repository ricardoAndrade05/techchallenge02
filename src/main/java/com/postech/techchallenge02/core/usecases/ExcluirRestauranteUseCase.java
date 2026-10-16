package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

public class ExcluirRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;

    public static ExcluirRestauranteUseCase getInstance(RestauranteGateway restauranteGateway) {
        return new ExcluirRestauranteUseCase(restauranteGateway);
    }

    public Restaurante execute(Long id) {
        return null;
    }

    private ExcluirRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

}
