package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
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

    public TipoUsuario execute(NovoTipoUsuarioDTO dto) {
        final boolean existe = this.tipoUsuarioGateway.existeTipoUsuario(dto.nomeTipo(),dto.idRestaurante(),dto.tipoUsuario());
        if (existe) {
            throw new TipoUsuarioExistenteException("Tipo " + dto.nomeTipo() + " já cadastrado");
        }
        Usuario usuarioCriador = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioCriador())
            .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário " + dto.idUsuarioCriador() + " não encontrado"));
        if (!usuarioCriador.podeCadastrarTipoUsuario()) {
            throw new UsuarioSemPermissaoException("Usuário " + dto.idUsuarioCriador() + " não tem permissão para criar tipo de usuário");
        }
        Restaurante restaurante = this.restauranteGateway.buscaRestaurantePorId(dto.idRestaurante())
            .orElseThrow(() -> new RestauranteNaoEncontradoException("Restaurante " + dto.idRestaurante() + " não encontrado"));
    
        final TipoUsuario novoTipoUsuario = TipoUsuario.criar(restaurante,dto.nomeTipo(),dto.tipoUsuario());

        return this.tipoUsuarioGateway.cadastraTipoUsuario(novoTipoUsuario);
    }

    private CadastrarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway, RestauranteGateway restauranteGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
        this.restauranteGateway = restauranteGateway;
    }

}
