package com.postech.techchallenge02.core.interfaces;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;

public interface TipoUsuarioGateway {

    TipoUsuario atualizarTipoUsuario(TipoUsuario tipoUsuario);

    Optional<TipoUsuario> buscaTipoUsuarioPorId(Long idTipoUsuario);

    Pagina<TipoUsuario> buscaTipoUsuarioPorRestaurante(Restaurante restaurante, Paginacao paginacao);

    TipoUsuario cadastraTipoUsuario(TipoUsuario novoTipoUsuario);

    boolean tipoUsuarioEstaVinculado(Long idTipoUsuario);

    boolean excluirTipoUsuario(Long idTipoUsuario);

    boolean existeTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO);

    boolean existeTipoUsuario(Long idTipoUsuario);

}

