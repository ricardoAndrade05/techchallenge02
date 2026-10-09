package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioVinculadoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class ExcluirTipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static ExcluirTipoUsuarioUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway) {
        return new ExcluirTipoUsuarioUseCase(tipoUsuarioGateway);
    }

    public boolean execute(Long idTipoUsuario) {
        boolean existeUsuario = this.tipoUsuarioGateway.existeTipoUsuario(idTipoUsuario);
        if (existeUsuario) {
            throw new TipoUsuarioNaoEncontradoException("Tipo de usuário não encontrado/inexistente: " + idTipoUsuario);
        }
        boolean estaVinculado = this.tipoUsuarioGateway.tipoUsuarioEstaVinculado(idTipoUsuario);
        if (estaVinculado) {
            throw new TipoUsuarioVinculadoException("O tipo de usuário está vinculado a um ou mais usuários.");
        }
        return this.tipoUsuarioGateway.excluirTipoUsuario(idTipoUsuario);
    }

    private ExcluirTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

}
