package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class AtualizarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;

    public static AtualizarRestauranteUseCase getInstance(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        return new AtualizarRestauranteUseCase(restauranteGateway, usuarioGateway);
    }

    public Restaurante execute(AtualizarRestauranteDTO dto) {
        Usuario usuarioLogado = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioLogado(), dto.idRestaurante())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário logado não encontrado para o restaurante informado."));

        Restaurante restaurante = this.restauranteGateway.buscaRestaurantePorId(dto.idRestaurante())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado para o id informado."));

        Usuario dono = restaurante.getDono();
        if (!usuarioLogado.podeAtualizar() || !dono.getId().equals(usuarioLogado.getId())) {
            throw new UsuarioSemPermissaoException("Usuário não tem permissão para editar/atualizar o restaurante.");
        }

        EnderecoDTO enderecoDto = dto.endereco();
        if (enderecoDto != null) {
            restaurante.getEndereco().atualizar(
                    enderecoDto.logradouro(),
                    enderecoDto.numero(),
                    enderecoDto.cidade(),
                    enderecoDto.estado(),
                    enderecoDto.cep());
        }
        Usuario novoUsuarioDono = null;
        if (dto.idDono() != null) {
            novoUsuarioDono = processarTrocaDeDono(restaurante, dto.idDono());
        }
        restaurante.atualizar(dto.nome(), dto.tipoCozinha(), dto.horaAbertura(), dto.horaFechamento(), novoUsuarioDono);
        return this.restauranteGateway.atualizar(restaurante);
    }

    private Usuario processarTrocaDeDono(Restaurante restaurante, Long idNovoDono) {
        Usuario donoAntigo = restaurante.getDono();

        if (donoAntigo != null && donoAntigo.getId().equals(idNovoDono)) {
            return donoAntigo;
        }
        Usuario novoDono = this.usuarioGateway.buscaUsuarioPorId(idNovoDono).orElseThrow(() -> new RecursoNaoEncontradoException("Novo proprietário não encontrado."));
        Usuario donoAntigoComContexto = esteUsuarioTemTipoNoRestaurante(donoAntigo.getId(), restaurante.getId());
        TipoUsuario tipoProprietario = donoAntigoComContexto != null ? donoAntigoComContexto.getTipoUsuario() : null;

        if (tipoProprietario != null) {
            this.usuarioGateway.buscaUsuarioPorId(novoDono.getId(), restaurante.getId())
                    .ifPresent(u -> {
                        if (u.getTipoUsuario() != null) {
                            this.usuarioGateway.desvincularUsuarioAoTipo(novoDono.getId(), u.getTipoUsuario().getId());
                        }
                    });
            this.usuarioGateway.desvincularUsuarioAoTipo(donoAntigo.getId(), tipoProprietario.getId());
            this.usuarioGateway.vincularUsuarioAoTipo(novoDono.getId(), tipoProprietario.getId());
        }

        return novoDono;
    }

    private Usuario esteUsuarioTemTipoNoRestaurante(Long idUsuario, Long idRestaurante) {
        return this.usuarioGateway.buscaUsuarioPorId(idUsuario, idRestaurante).orElse(null);
    }

    private AtualizarRestauranteUseCase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }
}