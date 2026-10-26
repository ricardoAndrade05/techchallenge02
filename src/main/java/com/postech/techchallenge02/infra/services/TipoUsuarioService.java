package com.postech.techchallenge02.infra.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.techchallenge02.core.controllers.TipoUsuarioController;
import com.postech.techchallenge02.core.dtos.AtualizarTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.ExcluirTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.infra.dtos.AtualizarTipoDTO;
import com.postech.techchallenge02.infra.dtos.ExcluiTipoDTO;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioController tipoUsuarioController;

    public TipoUsuarioService(TipoUsuarioDataSource tipoUsuarioDataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        this.tipoUsuarioController = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource);
    }

    @Transactional(readOnly = true)
    public TipoUsuarioDTO buscarPorId(Long idTipoUsuario) {
        return this.tipoUsuarioController.buscaTipoUsuarioPorId(idTipoUsuario);
    }

    @Transactional
    public TipoUsuarioDTO inserirTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        return this.tipoUsuarioController.inserirTipoUsuario(novoTipoUsuarioDTO);
    }

    @Transactional
    public void excluiTipoUsuario(Long id, ExcluiTipoDTO excluiTipoDTO) {
        this.tipoUsuarioController.excluirTipoUsuario(
                new ExcluirTipoUsuarioDTO(excluiTipoDTO.idUsuarioLogado(), id));
    }

    @Transactional
    public TipoUsuarioDTO atualizaTipoUsuario(Long id, AtualizarTipoDTO dto) {
        return this.tipoUsuarioController.atualizarTipoUsuario(
                new AtualizarTipoUsuarioDTO(id, dto.novoNome(), dto.idUsuarioLogado(), dto.idRestaurante()));
    }

    @Transactional(readOnly = true)
    public Page<TipoUsuarioDTO> buscaTiposUsuarioRestaurante(Long idRestaurante, Pageable pageable) {
        Pagina<TipoUsuarioDTO> pagina = this.tipoUsuarioController.buscaTiposUsuarioRestaurante(
                idRestaurante,
                new Paginacao(pageable.getPageNumber(), pageable.getPageSize()));
        return converterParaSpringPage(pagina);
    }

    private Page<TipoUsuarioDTO> converterParaSpringPage(Pagina<TipoUsuarioDTO> pagina) {
        Pageable pageable = PageRequest.of(pagina.paginaAtual(), pagina.tamanhoPagina());
        return new PageImpl<>(
                pagina.itens(),
                pageable,
                pagina.totalItens());
    }
}