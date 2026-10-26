package com.postech.techchallenge02.core.gateways;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioDataSource dataSource;
    private final TipoUsuarioDataSource tipoUsuarioDataSource;

    private UsuarioGatewayImpl(UsuarioDataSource dataSource, TipoUsuarioDataSource tipoUsuarioDataSource) {
        this.dataSource = dataSource;
        this.tipoUsuarioDataSource = tipoUsuarioDataSource;
    }

    public static UsuarioGateway getInstance(UsuarioDataSource dataSource, TipoUsuarioDataSource tipoUsuarioDataSource) {
        if (dataSource == null || tipoUsuarioDataSource == null) {
            throw new IllegalArgumentException("O data source não pode ser nulo");
        }
        return new UsuarioGatewayImpl(dataSource,tipoUsuarioDataSource);
    }
    
    @Override
    public Optional<Usuario> buscaUsuarioPorId(Long idUsuario, Long idRestaurante) {
        UsuarioDonoDTO usuarioDTO = this.dataSource.buscaUsuarioPorId(idUsuario,idRestaurante);
        if (usuarioDTO == null) {
            return Optional.empty();
        }
        TipoUsuarioDTO tipoUsuarioDTO = this.tipoUsuarioDataSource.buscaTipoUsuarioPorId(usuarioDTO.tipoUsuario().id());
        RestauranteDTO restauranteDTO = tipoUsuarioDTO.restaurante();
        Restaurante restaurante = Restaurante.restaurar(restauranteDTO.id(), restauranteDTO.nome(),null,null,null,null,null);
        TipoUsuario tipoUsuario = TipoUsuario.restaurar(tipoUsuarioDTO.id(), restaurante,tipoUsuarioDTO.nome(),tipoUsuarioDTO.tipo());
        Usuario usuario = Usuario.restaurar(usuarioDTO.id(), usuarioDTO.nome(),tipoUsuario);
        return Optional.of(usuario);
    }

    @Override
    public Optional<Usuario> buscaUsuarioPorId(Long idUsuario) {
        UsuarioDonoDTO usuarioDTO = this.dataSource.buscaUsuarioPorId(idUsuario);
        if (usuarioDTO == null) {
            return Optional.empty();
        }
        Usuario usuario = Usuario.restaurar(usuarioDTO.id(), usuarioDTO.nome(),null);
        return Optional.of(usuario);   
    }

    @Override
    public Usuario vincularUsuarioAoTipo(Long idUsuario, Long idTipoUsuario) {
        UsuarioDTO usuarioDTO = this.dataSource.vincularUsuarioAoTipo(idUsuario, idTipoUsuario);
        TipoUsuarioDTO tipoUsuarioDTO = usuarioDTO.tipoUsuario();
        TipoUsuario tipoUsuario = TipoUsuario.restaurar(tipoUsuarioDTO.id(), null, tipoUsuarioDTO.nome(), tipoUsuarioDTO.tipo());
        Usuario usuario = Usuario.restaurar(usuarioDTO.id(), usuarioDTO.nome(),tipoUsuario);
        return usuario;
    }

    @Override
    public boolean usuarioVinculadoAoRestaurante(Long idUsuario, Long idRestaurante) {
       return this.dataSource.usuarioEstaVinculadoAoRestaurante(idUsuario, idRestaurante);
    }

    @Override
    public void desvincularUsuarioAoTipo(Long idUsuario, Long idTipoUsuario) {
        this.dataSource.desvincularUsuarioAoTipo(idUsuario, idTipoUsuario);
    }

}
