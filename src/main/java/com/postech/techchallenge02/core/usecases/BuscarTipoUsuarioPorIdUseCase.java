package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class BuscarTipoUsuarioPorIdUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static BuscarTipoUsuarioPorIdUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway) {
        return new BuscarTipoUsuarioPorIdUseCase(tipoUsuarioGateway);
    }

    public TipoUsuario execute(Long id) {
        return this.tipoUsuarioGateway.buscaTipoUsuarioPorId(id)
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException("Não há tipo de usuário com id: " + id));
    }

    private BuscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

}
