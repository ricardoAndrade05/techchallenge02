package com.postech.techchallenge02.core.controllers;

import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.ExcluirRestauranteDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.gateways.RestauranteGatewayImpl;
import com.postech.techchallenge02.core.gateways.TipoUsuarioGatewayImpl;
import com.postech.techchallenge02.core.gateways.UsuarioGatewayImpl;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;
import com.postech.techchallenge02.core.presenters.RestaurantePresenter;
import com.postech.techchallenge02.core.usecases.AtualizarRestauranteUseCase;
import com.postech.techchallenge02.core.usecases.BuscarRestaurantePorIdUseCase;
import com.postech.techchallenge02.core.usecases.CadastrarRestauranteUseCase;
import com.postech.techchallenge02.core.usecases.ExcluirRestauranteUseCase;

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

    public void excluirRestaurante(ExcluirRestauranteDTO dto){
        RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource,this.tipoUsuariodataSource);
        ExcluirRestauranteUseCase.getInstance(restauranteGateway, usuarioGateway).execute(dto);
    }

    public RestauranteDTO atualizarRestaurante(AtualizarRestauranteDTO dto) {
        RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource,this.tipoUsuariodataSource);
        
        Restaurante restaurante = AtualizarRestauranteUseCase.getInstance(restauranteGateway, usuarioGateway).execute(dto);
        return RestaurantePresenter.toDTO(restaurante);
    }

    public RestauranteDTO cadastrarRestaurante(NovoRestauranteDTO dto) {
        RestauranteGateway restauranteGateway = RestauranteGatewayImpl.getInstance(this.restauranteDataSource);
        UsuarioGateway usuarioGateway = UsuarioGatewayImpl.getInstance(this.usuarioDataSource,this.tipoUsuariodataSource);
        TipoUsuarioGateway tipoUsuarioGateway = TipoUsuarioGatewayImpl.getInstance(tipoUsuariodataSource,usuarioDataSource);
        Restaurante restaurante = CadastrarRestauranteUseCase.getInstance(restauranteGateway, usuarioGateway, tipoUsuarioGateway).execute(dto);
        return RestaurantePresenter.toDTO(restaurante);
    }
    

}
