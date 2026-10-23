package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.DesassociarUsuarioTipoDTO;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class DesassociarUsuarioAoTipoUseCase {

    private final UsuarioGateway usuarioGateway;

    public static DesassociarUsuarioAoTipoUseCase getInstance(UsuarioGateway usuarioGateway) {
        return new DesassociarUsuarioAoTipoUseCase(usuarioGateway);
    }

    public void execute(DesassociarUsuarioTipoDTO dto) {

        Usuario usuarioLogado = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioLogado(), dto.idRestaurante())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado."));
        if (!usuarioLogado.podeAtualizar()) {
            throw new UsuarioSemPermissaoException("Usuário não tem permissão para realizar esta ação.");
        }

        Usuario usuarioADesassociar = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioADesassociar(),dto.idRestaurante())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario a ser desassociado não encontrado."));
        boolean usuarioJaVinculado = this.usuarioGateway.usuarioVinculadoAoRestaurante(dto.idUsuarioADesassociar(), dto.idRestaurante());
        if (!usuarioJaVinculado) {
            throw new UsuarioSemPermissaoException("Usuario já possui um tipo de usuario cadastrado para este restaurante.");
        }
        usuarioADesassociar = this.usuarioGateway.desvincularUsuarioAoTipo(usuarioADesassociar.getId(), usuarioADesassociar.getTipoUsuario().getId());
    }

    private DesassociarUsuarioAoTipoUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }
}
