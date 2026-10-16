package com.postech.techchallenge02.core.usecases;

import java.util.Objects;

import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class BuscarTipoUsuarioPorIdUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static BuscarTipoUsuarioPorIdUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway) {
        return new BuscarTipoUsuarioPorIdUseCase(tipoUsuarioGateway);
    }

    public TipoUsuario execute(Long id) {
        if (Objects.isNull(id)) {
            throw new DadosInvalidosException("Tipo de usuário inválido!");
        }
        return this.tipoUsuarioGateway.buscaTipoUsuarioPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Não há tipo de usuário com id: " + id));
    }

    private BuscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

}
