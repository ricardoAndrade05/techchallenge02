package com.postech.techchallenge02.core.controllers;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.gateways.RestauranteGatewayImpl;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.core.presenters.RestaurantePresenter;
import com.postech.techchallenge02.core.usecases.BuscarRestaurantePorIdUseCase;

public class RestauranteController {

    private final TipoUsuarioDataSource tipoUsuariodataSource;
    private final RestauranteDataSource restauranteDataSource;
    private final UsuarioDataSource usuarioDataSource;

    private RestauranteController(TipoUsuarioDataSource tipoUsuariodataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        this.tipoUsuariodataSource = tipoUsuariodataSource;
        this.restauranteDataSource = restauranteDataSource;
        this.usuarioDataSource = usuarioDataSource;
    }

    public static RestauranteController getInstance(TipoUsuarioDataSource tipoUsuariodataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        return new RestauranteController(tipoUsuariodataSource, restauranteDataSource, usuarioDataSource);
    }

    public RestauranteDTO buscarPorId(Long idRestaurante) {
        RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
        Restaurante restaurante = BuscarRestaurantePorIdUseCase.getInstance(restauranteGateway).execute(idRestaurante);
        
        return RestaurantePresenter.toDTO(restaurante);
    }
    

}
