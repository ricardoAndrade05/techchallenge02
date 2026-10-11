package com.postech.techchallenge02.core.controllers;

import com.postech.techchallenge02.core.dtos.AtualizarTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
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
import com.postech.techchallenge02.core.usecases.AtualizarTipoUsuarioUseCase;
import com.postech.techchallenge02.core.usecases.BuscarTipoUsuarioPorIdUseCase;
import com.postech.techchallenge02.core.usecases.CadastrarTipoUsuarioUseCase;
import com.postech.techchallenge02.core.usecases.ExcluirTipoUsuarioUseCase;

public class TipoUsuarioController {

    private final TipoUsuarioDataSource dataSource;
    private final RestauranteDataSource restauranteDataSource;
    private final UsuarioDataSource usuarioDataSource;

    private TipoUsuarioController(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        this.dataSource = dataSource;
        this.restauranteDataSource = restauranteDataSource;
        this.usuarioDataSource = usuarioDataSource;
    }

    public static TipoUsuarioController getInstance(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        return new TipoUsuarioController(dataSource, restauranteDataSource, usuarioDataSource);
    }

    public static TipoUsuarioController create(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        return new TipoUsuarioController(dataSource, restauranteDataSource, usuarioDataSource);
    }

    public TipoUsuarioDTO buscaTipoUsuarioPorId(long idTipoUsuario) {
        TipoUsuarioGateway gateway = TipoUsuarioGatewayImpl.getInstance(this.dataSource, this.restauranteDataSource);
        TipoUsuario tipoUsuario = BuscarTipoUsuarioPorIdUseCase.getInstance(gateway).execute(idTipoUsuario);

        return TipoUsuarioPresenter.toDTO(tipoUsuario);
    }

    public TipoUsuarioDTO inserirTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        TipoUsuarioGateway tipoUsuarioGateway = TipoUsuarioGatewayImpl.getInstance(this.dataSource, this.restauranteDataSource);
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource, this.restauranteDataSource, this.dataSource);
        RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
        TipoUsuario tipoUsuario = CadastrarTipoUsuarioUseCase.getInstance(tipoUsuarioGateway, usuarioGateway, restauranteGateway).execute(novoTipoUsuarioDTO);

        return TipoUsuarioPresenter.toDTO(tipoUsuario);
    }

    public void excluirTipoUsuario(ExcluirTipoUsuarioDTO excluirTipoUsuarioDTO) {
        TipoUsuarioGateway tipoUsuarioGateway = TipoUsuarioGatewayImpl.getInstance(this.dataSource, this.restauranteDataSource);
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource, this.restauranteDataSource, this.dataSource);

        ExcluirTipoUsuarioUseCase.getInstance(tipoUsuarioGateway, usuarioGateway).execute(excluirTipoUsuarioDTO);
    }

    public TipoUsuarioDTO atualizarTipoUsuario(AtualizarTipoUsuarioDTO atualizarTipoUsuarioDTO) {
        TipoUsuarioGateway tipoUsuarioGateway = TipoUsuarioGatewayImpl.getInstance(this.dataSource, this.restauranteDataSource);
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource, this.restauranteDataSource, this.dataSource);
        TipoUsuario tipoUsuario = AtualizarTipoUsuarioUseCase.getInstance(tipoUsuarioGateway, usuarioGateway).execute(atualizarTipoUsuarioDTO);

        return TipoUsuarioPresenter.toDTO(tipoUsuario);
    }

}
