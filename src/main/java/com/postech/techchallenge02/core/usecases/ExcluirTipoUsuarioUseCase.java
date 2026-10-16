package com.postech.techchallenge02.core.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioVinculadoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class ExcluirTipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final UsuarioGateway usuarioGateway;

    public static ExcluirTipoUsuarioUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        return new ExcluirTipoUsuarioUseCase(tipoUsuarioGateway, usuarioGateway);
    }

    public void execute(ExcluirTipoUsuarioDTO dto) {
        this.validaDadosEntrada(dto);
        
        TipoUsuario tipoUsuario = this.tipoUsuarioGateway.buscaTipoUsuarioPorId(dto.idTipoUsuario())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado/inexistente: " + dto.idTipoUsuario()));
        
        Usuario usuarioLogado = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioLogado(),tipoUsuario.getIdRestaurante())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado/inexistente para esse restaurante."));

        if (!usuarioLogado.podeExcluirTipoUsuario()) {
            throw new UsuarioSemPermissaoException("Usuário " + dto.idUsuarioLogado() + " não tem permissão para excluir tipo de usuário");
        }

        boolean estaVinculado = this.tipoUsuarioGateway.tipoUsuarioEstaVinculado(dto.idTipoUsuario());
        if (estaVinculado) {
            throw new TipoUsuarioVinculadoException("O tipo de usuário está vinculado a um ou mais usuários.");
        }

        this.tipoUsuarioGateway.excluirTipoUsuario(dto.idTipoUsuario());
    }

    private void validaDadosEntrada(ExcluirTipoUsuarioDTO dto) {
        List<String> erros = new ArrayList<>();

        if (Objects.isNull(dto.idUsuarioLogado())) {
            erros.add("Usuário inválido!");
        }
        if (Objects.isNull(dto.idTipoUsuario())) {
            erros.add("Tipo de usuário inválido!");
        }
        if (!erros.isEmpty()) {
            throw new DadosInvalidosException(String.join(" ", erros));
        }
    }

    private ExcluirTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
    }

}
