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
import com.postech.techchallenge02.infra.dtos.AtualizarTipoDTO;
import com.postech.techchallenge02.infra.dtos.ExcluiTipoDTO;
import com.postech.techchallenge02.infra.infra.mappers.RestauranteMapper;
import com.postech.techchallenge02.infra.infra.mappers.TipoUsuarioMapper;
import com.postech.techchallenge02.infra.infra.mappers.UsuarioMapper;
import com.postech.techchallenge02.infra.repositories.RestauranteRepository;
import com.postech.techchallenge02.infra.repositories.TipoUsuarioRepository;
import com.postech.techchallenge02.infra.repositories.UsuarioRepository;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    private final TipoUsuarioMapper tipoUsuarioMapper;
    private final UsuarioMapper usuarioMapper;
    private final RestauranteMapper restauranteMapper;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository, RestauranteRepository restauranteRepository, 
        UsuarioRepository usuarioRepository, TipoUsuarioMapper tipoUsuarioMapper, UsuarioMapper usuarioMapper, 
        RestauranteMapper restauranteMapper) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
        this.usuarioMapper = usuarioMapper;
        this.restauranteMapper = restauranteMapper;
        }

    public TipoUsuarioDTO buscarPorId(Long idTipoUsuario) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter(usuarioRepository,usuarioMapper);
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter(restauranteRepository, restauranteMapper);
        TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).buscaTipoUsuarioPorId(idTipoUsuario);
        return tipoUsuarioDTO;
    }

    public TipoUsuarioDTO inserirTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter(usuarioRepository,usuarioMapper);
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter(restauranteRepository, restauranteMapper);
        TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).inserirTipoUsuario(novoTipoUsuarioDTO);
        return tipoUsuarioDTO;
    }

    public void excluiTipoUsuario(Long id, ExcluiTipoDTO excluiTipoDTO) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter(usuarioRepository,usuarioMapper);
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter(restauranteRepository, restauranteMapper);
        TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).excluirTipoUsuario(new ExcluirTipoUsuarioDTO(excluiTipoDTO.idUsuarioLogado(), id));
    }

    public TipoUsuarioDTO atualizaTipoUsuario(Long id, AtualizarTipoDTO atualizarTipoDTO) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter(usuarioRepository,usuarioMapper);
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter(restauranteRepository, restauranteMapper);
        TipoUsuarioDTO tipoUsuarioDTO = TipoUsuarioController.getInstance(tipoUsuarioDataSource, restauranteDataSource, usuarioDataSource).atualizarTipoUsuario(new AtualizarTipoUsuarioDTO(id, atualizarTipoDTO.novoNome(), atualizarTipoDTO.idUsuarioLogado()));
        return tipoUsuarioDTO;
    }

    public Page<TipoUsuarioDTO> buscaTiposUsuarioRestaurante(Long idRestaurante, Pageable pageable) {
        TipoUsuarioDataSource tipoUsuarioDataSource = new TipoUsuarioDataSourceAdapter(tipoUsuarioRepository, restauranteRepository, tipoUsuarioMapper);
        UsuarioDataSource usuarioDataSource = new UsuarioDataSourceAdapter(usuarioRepository,usuarioMapper);
        RestauranteDataSource restauranteDataSource = new RestauranteDataSourceAdapter(restauranteRepository, restauranteMapper);
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
