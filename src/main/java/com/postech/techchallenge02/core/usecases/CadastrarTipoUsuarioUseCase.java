package com.postech.techchallenge02.core.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
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
        this.validaDadosEntrada(dto);
        final boolean existe = this.tipoUsuarioGateway.existeTipoUsuario(dto.nomeTipo(),dto.idRestaurante(),dto.tipoUsuario());
        if (existe) {
            throw new TipoUsuarioExistenteException("Tipo " + dto.nomeTipo() + " já cadastrado");
        }
        Usuario usuarioCriador = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioCriador(),dto.idRestaurante())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário " + dto.idUsuarioCriador() + " não encontrado"));
        if (!usuarioCriador.podeCadastrar()) {
            throw new UsuarioSemPermissaoException("Usuário " + dto.idUsuarioCriador() + " não tem permissão para criar tipo de usuário");
        }
        Restaurante restaurante = this.restauranteGateway.buscaRestaurantePorId(dto.idRestaurante())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante " + dto.idRestaurante() + " não encontrado"));
    
        final TipoUsuario novoTipoUsuario = TipoUsuario.criar(restaurante,dto.nomeTipo(),dto.tipoUsuario());

        return this.tipoUsuarioGateway.cadastraTipoUsuario(novoTipoUsuario);
    }

    private void validaDadosEntrada(NovoTipoUsuarioDTO dto) {
        List<String> erros = new ArrayList<>();

        if (Objects.isNull(dto.idRestaurante())) {
            erros.add("Restaurante inválido!");
        }
        if (Objects.isNull(dto.idUsuarioCriador())) {
            erros.add("Usuário criador inválido!");
        }
        if (!erros.isEmpty()) {
            throw new DadosInvalidosException(String.join(" ", erros));
        }
    }

    private CadastrarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway, RestauranteGateway restauranteGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
        this.restauranteGateway = restauranteGateway;
    }

}
