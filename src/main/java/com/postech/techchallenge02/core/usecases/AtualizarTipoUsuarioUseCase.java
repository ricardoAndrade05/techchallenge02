package com.postech.techchallenge02.core.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.postech.techchallenge02.core.dtos.AtualizarTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class AtualizarTipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final UsuarioGateway usuarioGateway;

    public static AtualizarTipoUsuarioUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        return new AtualizarTipoUsuarioUseCase(tipoUsuarioGateway, usuarioGateway);
    }

    public TipoUsuario execute(AtualizarTipoUsuarioDTO dto) {
        validaDadosEntrada(dto);
        Usuario usuarioLogado = usuarioGateway.buscaUsuarioPorId(dto.idUsuarioLogado())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao encontrado"));
        if (!usuarioLogado.podeAtualizarTipoUsuario()) {
            throw new UsuarioSemPermissaoException("Usuario nao tem permissao para atualizar tipo de usuario");
        }
        TipoUsuario tipoUsuario = this.tipoUsuarioGateway.buscaTipoUsuarioPorId(dto.idTipoUsuario())
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException("Tipo de usuário não encontrado"));
        boolean existe = this.tipoUsuarioGateway.existeTipoUsuario(dto.novoNome(), tipoUsuario.getIdRestaurante(), tipoUsuario.getTipo());
        if (existe) {
            throw new TipoUsuarioExistenteException("Não é possivel atualizar esse tipo usuario pois o mesmo ja existe!");
        }
        tipoUsuario.atualizar(dto.novoNome());
        return this.tipoUsuarioGateway.atualizarTipoUsuario(tipoUsuario);
    }

    private void validaDadosEntrada(AtualizarTipoUsuarioDTO dto) {
        List<String> erros = new ArrayList<>();

        if (Objects.isNull(dto.idTipoUsuario())) {
            erros.add("Tipo de usuário inválido!");
        }
        if (Objects.isNull(dto.idUsuarioLogado())) {
            erros.add("Usuário logado inválido!");
        }
        if (!erros.isEmpty()) {
            throw new DadosInvalidosException(String.join(" ", erros));
        }
    }

    private AtualizarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
    }

}
