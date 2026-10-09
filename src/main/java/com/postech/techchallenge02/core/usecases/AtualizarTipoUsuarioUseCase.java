package com.postech.techchallenge02.core.usecases;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class AtualizarTipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static AtualizarTipoUsuarioUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway) {
        return new AtualizarTipoUsuarioUseCase(tipoUsuarioGateway);
    }

    public TipoUsuario execute(TipoUsuarioDTO tipoUsuarioDTO) {
        Optional<TipoUsuario> tipoUsuario = this.tipoUsuarioGateway.buscaTipoUsuarioPorId(tipoUsuarioDTO.id());
        if (tipoUsuario.isEmpty()) {
            new TipoUsuarioNaoEncontradoException("Tipo de usuário não encontrado");
        }
        tipoUsuario.get().atualizar(tipoUsuarioDTO.nome(),tipoUsuarioDTO.tipoUsuario());
        return this.tipoUsuarioGateway.atualizarTipoUsuario(tipoUsuario.get());
    }

    private AtualizarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

}
