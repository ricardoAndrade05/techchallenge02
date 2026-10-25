package com.postech.techchallenge02.infra.adapters;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;
import com.postech.techchallenge02.infra.infra.mappers.UsuarioMapper;
import com.postech.techchallenge02.infra.repositories.TipoUsuarioRepository;
import com.postech.techchallenge02.infra.repositories.UsuarioRepository;

@Component 
public class UsuarioDataSourceAdapter implements UsuarioDataSource {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioDataSourceAdapter(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, TipoUsuarioRepository tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioDonoDTO buscaUsuarioPorId(Long idUsuario, Long idRestaurante) {
        return this.usuarioRepository.findById(idUsuario)
                .flatMap(usuario -> usuario.getTiposUsuario().stream()
                        .filter(tu -> tu.getRestaurante() != null && idRestaurante.equals(tu.getRestaurante().getId()))
                        .findFirst()
                        .map(tipoUsuario -> this.usuarioMapper.toUsuarioDonoDTO(usuario, tipoUsuario)))
                .orElse(null);
    }

    @Override
    public boolean existeUsuarioComEsteTipoUsuario(Long idTipoUsuario) {
        return this.usuarioRepository.existeUsuarioComEsteTipoUsuario(idTipoUsuario);
    }

    @Override
    public UsuarioDonoDTO buscaUsuarioPorId(Long idUsuario) {
        return this.usuarioRepository.findById(idUsuario)
                .map(usuario -> this.usuarioMapper.toUsuarioDonoDTO(usuario))
                .orElse(null);
    }

    @Override
    public boolean usuarioEstaVinculadoAoRestaurante(Long idUsuario, Long idRestaurante) {
        return this.usuarioRepository.usuarioEstaVinculadoAoRestaurante(idUsuario, idRestaurante);
    }

    @Override
    public UsuarioDTO vincularUsuarioAoTipo(Long idUsuario, Long idTipoUsuario) {
        Usuario usuario = this.usuarioRepository.findById(idUsuario).orElse(null);
        TipoUsuario tiposUsuario = this.tipoUsuarioRepository.findById(idTipoUsuario).orElse(null);
        usuario.addTiposUsuario(tiposUsuario);
        usuario = this.usuarioRepository.saveAndFlush(usuario);
        return this.usuarioMapper.toDTO(usuario,tiposUsuario);
    }

    @Override
    public void desvincularUsuarioAoTipo(Long idUsuario, Long idTipoUsuario) {
        Usuario usuario = this.usuarioRepository.findById(idUsuario).orElse(null);
        TipoUsuario tiposUsuario = this.tipoUsuarioRepository.findById(idTipoUsuario).orElse(null);
        usuario.removeTiposUsuario(tiposUsuario);
        this.usuarioRepository.saveAndFlush(usuario);
    }

}
