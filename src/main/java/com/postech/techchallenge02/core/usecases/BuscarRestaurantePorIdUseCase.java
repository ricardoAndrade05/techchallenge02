package com.postech.techchallenge02.core.usecases;

import java.util.Objects;

import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

public class BuscarRestaurantePorIdUseCase {

    private final RestauranteGateway restauranteGateway;

    public static BuscarRestaurantePorIdUseCase getInstance(RestauranteGateway restauranteGateway) {
        return new BuscarRestaurantePorIdUseCase(restauranteGateway);
    }

    public Restaurante execute(Long id) {
        if (Objects.isNull(id)) {
            throw new DadosInvalidosException("Restaurante inválido.");
        }
        Restaurante restaurante = this.restauranteGateway.buscaRestaurantePorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não há restaurante com id: " + id));
        return restaurante;
    }

    private BuscarRestaurantePorIdUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

}
