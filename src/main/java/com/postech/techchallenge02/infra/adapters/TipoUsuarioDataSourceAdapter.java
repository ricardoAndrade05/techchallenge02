package com.postech.techchallenge02.infra.adapters;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.infra.entities.Restaurante;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.infra.mappers.TipoUsuarioMapper;
import com.postech.techchallenge02.infra.repositories.RestauranteRepository;
import com.postech.techchallenge02.infra.repositories.TipoUsuarioRepository;

public class TipoUsuarioDataSourceAdapter implements TipoUsuarioDataSource {

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final RestauranteRepository restauranteRepository;  
    private final TipoUsuarioMapper tipoUsuarioMapper;

    public TipoUsuarioDataSourceAdapter(TipoUsuarioRepository tipoUsuarioRepository,RestauranteRepository restauranteRepository, TipoUsuarioMapper tipoUsuarioMapper) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.restauranteRepository = restauranteRepository;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    @Override
    public TipoUsuarioDTO buscaTipoUsuarioPorId(Long idTipoUsuario) {
        return this.tipoUsuarioRepository.findById(idTipoUsuario)
                .map(tipoUsuario -> {
                    Long idRestaurante = tipoUsuario.getRestaurante() != null ? tipoUsuario.getRestaurante().getId() : null;
                    return new TipoUsuarioDTO(
                            tipoUsuario.getId(),
                            idRestaurante,
                            tipoUsuario.getNome(),
                            tipoUsuario.getTipo());
                })
                .orElse(null);
    }

    @Override
    public TipoUsuarioDTO cadastraTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        TipoUsuario tipoUsuario = this.tipoUsuarioMapper.toEntity(novoTipoUsuarioDTO);
        Restaurante restaurante = this.restauranteRepository.findById(novoTipoUsuarioDTO.idRestaurante()).get();
        tipoUsuario.setRestaurante(restaurante);      
        tipoUsuario = this.tipoUsuarioRepository.save(tipoUsuario);
        return this.tipoUsuarioMapper.toDTO(tipoUsuario);
    }

    @Override
    public boolean excluirTipoUsuario(Long idTipoUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'excluirTipoUsuario'");
    }

    @Override
    public TipoUsuarioDTO atualizar(TipoUsuarioDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }

    @Override
    public boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante, TipoUsuarioEnun tipoUsuarioEnun) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeTipoUsuario'");
    }

    @Override
    public boolean existeTipoUsuario(String nome, Long idRestaurante, TipoUsuarioEnun tipoUsuarioEnun) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeTipoUsuario'");
    }

    @Override
    public Pagina<TipoUsuarioDTO> buscaTiposUsuarioPorRestaurante(Long idRestaurante, Paginacao paginacao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscaTiposUsuarioPorRestaurante'");
    }

}
