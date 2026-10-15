package com.postech.techchallenge02.core.interfaces;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;

public interface TipoUsuarioGateway {

    TipoUsuario atualizarTipoUsuario(TipoUsuario tipoUsuario);

    Optional<TipoUsuario> buscaTipoUsuarioPorId(Long idTipoUsuario);

    Pagina<TipoUsuario> buscaTiposUsuarioPorRestaurante(Restaurante restaurante, Paginacao paginacao);

    TipoUsuario cadastraTipoUsuario(TipoUsuario novoTipoUsuario);

    boolean tipoUsuarioEstaVinculado(Long idTipoUsuario);

    void excluirTipoUsuario(Long idTipoUsuario);

    boolean existeTipoUsuario(String nome, Long idRestaurante, TipoUsuarioEnun tipoUsuarioEnun);

    boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante);

}

