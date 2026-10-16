package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

public class CadastrarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;

    public static CadastrarRestauranteUseCase getInstance(RestauranteGateway restauranteGateway) {
        return new CadastrarRestauranteUseCase(restauranteGateway);
    }

    public Restaurante execute(Restaurante restaurante) {
        return null;
    }

    private CadastrarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

}
