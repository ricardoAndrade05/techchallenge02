package com.postech.techchallenge02.core.controllers;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.gateways.RestauranteGatewayImpl;
import com.postech.techchallenge02.core.gateways.TipoUsuarioGatewayImpl;
import com.postech.techchallenge02.core.gateways.UsuarioGatewayImpl;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;
import com.postech.techchallenge02.core.presenters.TipoUsuarioPresenter;
import com.postech.techchallenge02.core.usecases.BuscarTipoUsuarioPorIdUseCase;
import com.postech.techchallenge02.core.usecases.CadastrarTipoUsuarioUseCase;

public class TipoUsuarioController {

    private TipoUsuarioDataSource dataSource;
    private RestauranteDataSource restauranteDataSource;
    private UsuarioDataSource usuarioDataSource;

    private TipoUsuarioController(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        this.dataSource = dataSource;
        this.restauranteDataSource = restauranteDataSource;     
        this.usuarioDataSource = usuarioDataSource;
    }

    public static TipoUsuarioController getInstance(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        return new TipoUsuarioController(dataSource, restauranteDataSource, usuarioDataSource);
    }

    public TipoUsuarioDTO buscaTipoUsuarioPorId(long idTipoUsuario) {
        TipoUsuarioGateway gateway = TipoUsuarioGatewayImpl.getInstance(this.dataSource, this.restauranteDataSource);
        TipoUsuario tipoUsuario = BuscarTipoUsuarioPorIdUseCase.getInstance(gateway).execute(idTipoUsuario);
        return TipoUsuarioPresenter.toDTO(tipoUsuario);
    }

    public TipoUsuarioDTO inserirTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO){
        TipoUsuarioGateway tipoUsuarioGatway = TipoUsuarioGatewayImpl.getInstance(this.dataSource, this.restauranteDataSource);
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource, this.restauranteDataSource,this.dataSource);
        RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
        TipoUsuario tipoUsuario = CadastrarTipoUsuarioUseCase.getInstance(tipoUsuarioGatway, usuarioGateway, restauranteGateway).execute(novoTipoUsuarioDTO);
        return TipoUsuarioPresenter.toDTO(tipoUsuario);
    }

}
