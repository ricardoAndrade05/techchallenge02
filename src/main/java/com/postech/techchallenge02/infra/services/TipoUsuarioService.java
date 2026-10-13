package com.postech.techchallenge02.infra.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import com.postech.techchallenge02.infra.adapters.RestauranteDataSourceAdapter;
import com.postech.techchallenge02.infra.adapters.TipoUsuarioDataSourceAdapter;
import com.postech.techchallenge02.infra.adapters.UsuarioDataSourceAdapter;
import com.postech.techchallenge02.infra.infra.mappers.TipoUsuarioMapper;
import com.postech.techchallenge02.infra.repositories.RestauranteRepository;
import com.postech.techchallenge02.infra.repositories.TipoUsuarioRepository;

@Service
public class TipoUsuarioService {

    private TipoUsuarioRepository tipoUsuarioRepository;
    private final TipoUsuarioMapper tipoUsuarioMapper;
    private final RestauranteRepository restauranteRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository, RestauranteRepository restauranteRepository, TipoUsuarioMapper tipoUsuarioMapper) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.restauranteRepository = restauranteRepository;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    public TipoUsuarioDTO buscarPorId(Long idTipoUsuario) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter();
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter();
        TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).buscaTipoUsuarioPorId(idTipoUsuario);
        return tipoUsuarioDTO;
    }

    public TipoUsuarioDTO inserirTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter();
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter();
        TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).inserirTipoUsuario(novoTipoUsuarioDTO);
        return tipoUsuarioDTO;
    }

    public void excluiTipoUsuario(ExcluirTipoUsuarioDTO excluirTipoUsuarioDTO) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter();
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter();
        TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).excluirTipoUsuario(excluirTipoUsuarioDTO);
    }

    public TipoUsuarioDTO atualizaTipoUsuario(AtualizarTipoUsuarioDTO atualizarTipoUsuarioDTO) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter();
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter();
        TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).atualizarTipoUsuario(atualizarTipoUsuarioDTO);
        return tipoUsuarioDTO;
    }

    public Page<TipoUsuarioDTO> buscaTiposUsuarioRestaurante(Long idRestaurante, Pageable pageable) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter();
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter();
        Pagina<TipoUsuarioDTO> pagina = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).buscaTiposUsuarioRestaurante(idRestaurante, new Paginacao(pageable.getPageNumber(), pageable.getPageSize()));
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
