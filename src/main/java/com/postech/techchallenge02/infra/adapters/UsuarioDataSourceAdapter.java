package com.postech.techchallenge02.infra.adapters;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.infra.infra.mappers.UsuarioMapper;
import com.postech.techchallenge02.infra.repositories.UsuarioRepository;

public class UsuarioDataSourceAdapter implements UsuarioDataSource {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioDataSourceAdapter(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioDTO buscaUsuarioPorId(Long idUsuario, Long idRestaurante) {
        return this.usuarioRepository.findById(idUsuario)
                .flatMap(usuario -> usuario.getTiposUsuario().stream()
                        .filter(tu -> tu.getRestaurante() != null && idRestaurante.equals(tu.getRestaurante().getId()))
                        .findFirst()
                        .map(tipoUsuario -> this.usuarioMapper.toDTO(usuario, tipoUsuario)))
                .orElse(null);
    }

    @Override
    public boolean existeUsuarioComEsteTipoUsuario(Long idTipoUsuario) {
        return this.usuarioRepository.existeUsuarioComEsteTipoUsuario(idTipoUsuario);
    }

}
