package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class BuscarTipoUsuarioPorRestauranteUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static BuscarTipoUsuarioPorRestauranteUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway) {
        return new BuscarTipoUsuarioPorRestauranteUseCase(tipoUsuarioGateway);
    }

    public Pagina<TipoUsuario> execute(Restaurante restaurante, Paginacao paginacao) {
        final Pagina<TipoUsuario> tiposUsuarios = this.tipoUsuarioGateway.buscaTipoUsuarioPorRestaurante(restaurante, paginacao);
        if (tiposUsuarios.estaVazia()) {
            throw new TipoUsuarioNaoEncontradoException("Não há tipos de usuários cadastrados para o restaurante: " + restaurante.getNome());
        }
        return tiposUsuarios;
    }

    public Pagina<TipoUsuario> execute(Restaurante restaurante) {
        return execute(restaurante, new Paginacao(0, 10));
    }

    private BuscarTipoUsuarioPorRestauranteUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }
}
