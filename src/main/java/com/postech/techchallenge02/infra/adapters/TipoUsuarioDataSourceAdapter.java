package com.postech.techchallenge02.infra.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.infra.infra.mappers.TipoUsuarioMapper;
import com.postech.techchallenge02.infra.jpa.entities.RestauranteEntity;
import com.postech.techchallenge02.infra.jpa.entities.TipoUsuarioEntity;
import com.postech.techchallenge02.infra.jpa.repositories.RestauranteRepository;
import com.postech.techchallenge02.infra.jpa.repositories.TipoUsuarioRepository;

@Component 
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
                    RestauranteEntity restaurante = tipoUsuario.getRestaurante();
                    RestauranteDTO restauranteDTO = new RestauranteDTO(restaurante.getId(), restaurante.getNome(),null,null,null,null,null);
                    return new TipoUsuarioDTO(
                            tipoUsuario.getId(),
                            restauranteDTO,
                            tipoUsuario.getNome(),
                            tipoUsuario.getTipo());
                })
                .orElse(null);
    }

    @Override
    public TipoUsuarioDTO cadastraTipoUsuario(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        TipoUsuarioEntity tipoUsuario = this.tipoUsuarioMapper.toEntity(novoTipoUsuarioDTO);
        RestauranteEntity restaurante = this.restauranteRepository.findById(novoTipoUsuarioDTO.idRestaurante()).get();
        tipoUsuario.setRestaurante(restaurante);      
        tipoUsuario = this.tipoUsuarioRepository.save(tipoUsuario);
        return this.tipoUsuarioMapper.toDTO(tipoUsuario);
    }

    @Override
    public void excluirTipoUsuario(Long idTipoUsuario) {
        this.tipoUsuarioRepository.deleteById(idTipoUsuario);
    }

    @Override
    public TipoUsuarioDTO atualizar(TipoUsuarioDTO dto) {
        TipoUsuarioEntity tipoUsuario = this.tipoUsuarioRepository.findById(dto.id()).get();
        tipoUsuario.setNome(dto.nome());
        this.tipoUsuarioRepository.save(tipoUsuario);
        return this.tipoUsuarioMapper.toDTO(tipoUsuario);
    }

    @Override
    public boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante) {
        return this.tipoUsuarioRepository.existeTipoUsuario(idTipoUsuario,idRestaurante);
    }

    @Override
    public boolean existeTipoUsuario(String nome, Long idRestaurante, CategoriaEnun tipoUsuarioEnun) {
        return this.tipoUsuarioRepository.existeTipoUsuario(nome,idRestaurante, tipoUsuarioEnun);
    }

    @Override
    public Pagina<TipoUsuarioDTO> buscaTiposUsuarioPorRestaurante(Long idRestaurante, Paginacao paginacao) {
        Pageable pageable = PageRequest.of(paginacao.pagina(), paginacao.tamanho());
        Page<TipoUsuarioEntity> paginaEntity = this.tipoUsuarioRepository.buscaTiposUsuarioPorRestaurante(idRestaurante, pageable);
        Page<TipoUsuarioDTO> pageDTO = paginaEntity.map(this.tipoUsuarioMapper::toDTO);
        return new Pagina<TipoUsuarioDTO>(pageDTO.getContent(), pageDTO.getNumber(), pageDTO.getSize(), pageDTO.getTotalElements(), pageDTO.getTotalPages());   
    }
}
