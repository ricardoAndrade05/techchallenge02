package com.postech.techchallenge02.core.controllers;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.gateways.TipoUsuarioGatewayImpl;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.presenters.TipoUsuarioPresenter;
import com.postech.techchallenge02.core.usecases.BuscarTipoUsuarioPorIdUseCase;

public class TipoUsuarioController {

    private TipoUsuarioDataSource dataSource;
    private RestauranteDataSource restauranteDataSource;  

    private TipoUsuarioController(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource) {
        this.dataSource = dataSource;
        this.restauranteDataSource = restauranteDataSource;
    }

    public static TipoUsuarioController getInstance(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource) {
        return new TipoUsuarioController(dataSource, restauranteDataSource);
    }

    public TipoUsuarioDTO buscaTipoUsuarioPorId(long idTipoUsuario) {
        TipoUsuarioGateway gateway = TipoUsuarioGatewayImpl.getInstance(this.dataSource, this.restauranteDataSource);
        TipoUsuario tipoUsuario = BuscarTipoUsuarioPorIdUseCase.getInstance(gateway).execute(idTipoUsuario);
        return TipoUsuarioPresenter.toDTO(tipoUsuario);
    }

}
