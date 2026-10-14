package com.postech.techchallenge02.core.gateways;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioDataSource dataSource;
    private final RestauranteDataSource restauranteDataSource;
    private final TipoUsuarioDataSource tipoUsuarioDataSource;

    private UsuarioGatewayImpl(UsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, TipoUsuarioDataSource tipoUsuarioDataSource) {
        this.dataSource = dataSource;
        this.restauranteDataSource = restauranteDataSource;
        this.tipoUsuarioDataSource = tipoUsuarioDataSource;
    }

    public static UsuarioGateway getInstance(UsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, TipoUsuarioDataSource tipoUsuarioDataSource) {
        if (dataSource == null || restauranteDataSource == null || tipoUsuarioDataSource == null) {
            throw new IllegalArgumentException("O data source não pode ser nulo");
        }
        return new UsuarioGatewayImpl(dataSource, restauranteDataSource, tipoUsuarioDataSource);
    }
    
    @Override
    public Optional<Usuario> buscaUsuarioPorId(Long idUsuario) {
        UsuarioDTO usuarioDTO = this.dataSource.buscaUsuarioPorId(idUsuario);
        if (usuarioDTO == null) {
            return Optional.empty();
        }
        TipoUsuarioDTO tipoUsuarioDTO = this.tipoUsuarioDataSource.buscaTipoUsuarioPorId(usuarioDTO.idTipoUsuario());
        RestauranteDTO restauranteDTO = tipoUsuarioDTO.restaurante();
        Restaurante restaurante = Restaurante.restaurar(restauranteDTO.id(), restauranteDTO.nome());
        TipoUsuario tipoUsuario = TipoUsuario.restaurar(tipoUsuarioDTO.id(), restaurante,tipoUsuarioDTO.nome(),tipoUsuarioDTO   .tipo());
        Usuario usuario = Usuario.restaurar(usuarioDTO.id(), usuarioDTO.nome(),tipoUsuario);
        return Optional.of(usuario);
    }

}
