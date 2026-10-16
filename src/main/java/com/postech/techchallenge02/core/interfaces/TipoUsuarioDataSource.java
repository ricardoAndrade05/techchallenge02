package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.enums.CategoriaEnun;

public interface TipoUsuarioDataSource {

    TipoUsuarioDTO buscaTipoUsuarioPorId(Long idTipoUsuario);

    TipoUsuarioDTO cadastraTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO);

    void excluirTipoUsuario(Long idTipoUsuario);

    TipoUsuarioDTO atualizar(TipoUsuarioDTO dto);

    boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante);

    boolean existeTipoUsuario(String nome, Long idRestaurante, CategoriaEnun tipoUsuarioEnun);

    Pagina<TipoUsuarioDTO> buscaTiposUsuarioPorRestaurante(Long idRestaurante, Paginacao paginacao);
    

}
