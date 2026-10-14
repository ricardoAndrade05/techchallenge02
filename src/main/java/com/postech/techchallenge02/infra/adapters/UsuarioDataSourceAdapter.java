package com.postech.techchallenge02.infra.adapters;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.infra.entities.Usuario;
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
    public UsuarioDTO buscaUsuarioPorId(Long idUsuario) {
        Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            return null;
        }
        return this.usuarioMapper.toDTO(usuarioOptional.get());
    }

    @Override
    public boolean existeUsuarioComEsteTipoUsuario(Long idTipoUsuario) {
       return this.usuarioRepository.existeUsuarioComEsteTipoUsuario(idTipoUsuario);
    }

}
