package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.exceptions.RestauranteNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class BuscarTipoUsuarioPorRestauranteUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final RestauranteGateway restauranteGateway;

    public static BuscarTipoUsuarioPorRestauranteUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway, RestauranteGateway restauranteGateway) {
        return new BuscarTipoUsuarioPorRestauranteUseCase(tipoUsuarioGateway, restauranteGateway);
    }

    public Pagina<TipoUsuario> execute(Long idRestaurante, Paginacao paginacao) {
        Restaurante restaurante = restauranteGateway.buscaRestaurantePorId(idRestaurante)
        .orElseThrow(() -> new RestauranteNaoEncontradoException("Restaurante não encontrado"));

        final Pagina<TipoUsuario> tiposUsuarios = this.tipoUsuarioGateway.buscaTiposUsuarioPorRestaurante(restaurante, paginacao);
        if (tiposUsuarios.estaVazia()) {
            throw new TipoUsuarioNaoEncontradoException("Não há tipos de usuários cadastrados para o restaurante: " + idRestaurante);
        }
        return tiposUsuarios;
    }

    public Pagina<TipoUsuario> execute(Long idRestaurante) {
        return execute(idRestaurante, new Paginacao(0, 10));
    }

    private BuscarTipoUsuarioPorRestauranteUseCase(TipoUsuarioGateway tipoUsuarioGateway,RestauranteGateway restauranteGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.restauranteGateway = restauranteGateway;
    }
}
