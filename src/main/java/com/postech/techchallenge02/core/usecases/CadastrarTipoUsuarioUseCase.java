package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.exceptions.RestauranteNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
import com.postech.techchallenge02.core.exceptions.UsuarioNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class CadastrarTipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final UsuarioGateway usuarioGateway;
    private final RestauranteGateway restauranteGateway;

    public static CadastrarTipoUsuarioUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway, RestauranteGateway restauranteGateway) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuarioGateway, usuarioGateway, restauranteGateway);
    }

    public TipoUsuario execute(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        final boolean existe = this.tipoUsuarioGateway.existeTipoUsuario(novoTipoUsuarioDTO);
        if (existe) {
            throw new TipoUsuarioExistenteException("Tipo " + novoTipoUsuarioDTO.nomeTipo() + " já cadastrado");
        }
        Usuario usuarioCriador = this.usuarioGateway.buscaUsuarioPorId(novoTipoUsuarioDTO.idUsuarioCriador())
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário " + novoTipoUsuarioDTO.idUsuarioCriador() + " não encontrado"));
        if (!usuarioCriador.podeCadastrarTipoUsuario()) {
            throw new UsuarioSemPermissaoException("Usuário " + novoTipoUsuarioDTO.idUsuarioCriador() + " não tem permissão para criar tipo de usuário");
        }
        Restaurante restaurante = this.restauranteGateway.buscaRestaurantePorId(novoTipoUsuarioDTO.idRestaurante())
            .orElseThrow(() -> new RestauranteNaoEncontradoException("Restaurante " + novoTipoUsuarioDTO.idRestaurante() + " não encontrado"));
    
        final TipoUsuario novoTipoUsuario = TipoUsuario.criar(restaurante,novoTipoUsuarioDTO.nomeTipo(),novoTipoUsuarioDTO.tipoUsuario());

        return this.tipoUsuarioGateway.cadastraTipoUsuario(novoTipoUsuario);
    }

    private CadastrarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway, RestauranteGateway restauranteGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
        this.restauranteGateway = restauranteGateway;
    }

}
