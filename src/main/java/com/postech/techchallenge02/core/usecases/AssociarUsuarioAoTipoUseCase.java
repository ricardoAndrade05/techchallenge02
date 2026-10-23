package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.AssociarUsuarioAoTipoDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioJaVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class AssociarUsuarioAoTipoUseCase {

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static AssociarUsuarioAoTipoUseCase getInstance(UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        return new AssociarUsuarioAoTipoUseCase(usuarioGateway, tipoUsuarioGateway);
    }

    public Usuario execute(AssociarUsuarioAoTipoDTO dto) {
        Usuario usuarioLogado = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioLogado(), dto.idRestaurante())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado."));
        if (!usuarioLogado.podeCadastrar()) {
            throw new UsuarioSemPermissaoException("Usuário não tem permissão para realizar esta ação.");
        }
        TipoUsuario tipoUsuario = this.tipoUsuarioGateway.buscaTipoUsuarioPorId(dto.idTipoUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuario não encontrado."));
        if(!tipoUsuario.getIdRestaurante().equals(dto.idRestaurante())) {
            throw new UsuarioSemPermissaoException("O tipo usuario não pertence ao restaurante do usuario logado.");
        }
        Usuario usuarioAVinculado = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioAAssociar())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario a ser vinculado não encontrado."));
        boolean usuarioJaVinculado = this.usuarioGateway.usuarioVinculadoAoRestaurante(dto.idUsuarioAAssociar(), dto.idRestaurante());
        if(usuarioJaVinculado) {
            throw new UsuarioJaVinculadoException("Usuario já possui um tipo de usuario cadastrado para este restaurante.");
        }
        usuarioAVinculado = this.usuarioGateway.vincularUsuarioAoTipo(dto.idUsuarioAAssociar(), dto.idTipoUsuario());
        return usuarioAVinculado;
    }

    private AssociarUsuarioAoTipoUseCase(UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }
}
