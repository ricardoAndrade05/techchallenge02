package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class ExcluirTipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final UsuarioGateway usuarioGateway;

    public static ExcluirTipoUsuarioUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        return new ExcluirTipoUsuarioUseCase(tipoUsuarioGateway, usuarioGateway);
    }

    public void execute(ExcluirTipoUsuarioDTO excluirTipoUsuarioDTO) {
        Usuario usuarioLogado = this.usuarioGateway.buscaUsuarioPorId(excluirTipoUsuarioDTO.idUsuarioLogado())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado/inexistente: " + excluirTipoUsuarioDTO.idUsuarioLogado()));

        if (!usuarioLogado.podeExcluirTipoUsuario()) {
            throw new UsuarioSemPermissaoException("Usuário " + excluirTipoUsuarioDTO.idUsuarioLogado() + " não tem permissão para excluir tipo de usuário");
        }

        boolean existeTipoUsuario = this.tipoUsuarioGateway.existeTipoUsuario(excluirTipoUsuarioDTO.idTipoUsuario());
        if (!existeTipoUsuario) {
            throw new TipoUsuarioNaoEncontradoException("Tipo de usuário não encontrado/inexistente: " + excluirTipoUsuarioDTO.idTipoUsuario());
        }

        boolean estaVinculado = this.tipoUsuarioGateway.tipoUsuarioEstaVinculado(excluirTipoUsuarioDTO.idTipoUsuario());
        if (estaVinculado) {
            throw new TipoUsuarioVinculadoException("O tipo de usuário está vinculado a um ou mais usuários.");
        }

        this.tipoUsuarioGateway.excluirTipoUsuario(excluirTipoUsuarioDTO.idTipoUsuario());
    }

    private ExcluirTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
    }

}
