package com.postech.techchallenge02.core.gateways;

import java.util.Optional;

import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.UsuarioDonoDTO;
import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;

public class RestauranteGatewayImpl implements RestauranteGateway {

    private final RestauranteDataSource dataSource;

    private RestauranteGatewayImpl(RestauranteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RestauranteGateway getInstance(RestauranteDataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("O data source não pode ser nulo");
        }
        return new RestauranteGatewayImpl(dataSource);
    }

    @Override
    public Optional<Restaurante> buscaRestaurantePorId(Long idRestaurante) {
        return Optional.ofNullable(this.dataSource.buscaRestaurantePorId(idRestaurante)).map(this::mapearParaEntidade);
    }

    @Override
    public void excluirRestaurante(Long idRestaurante) {
        this.dataSource.excluirRestaurante(idRestaurante);
    }

    @Override
    public Restaurante cadastrar(Restaurante restaurante) {
        Endereco endereco = restaurante.getEndereco();
        EnderecoDTO enderecoDTO = new EnderecoDTO(null, endereco.getLogradouro(), endereco.getNumero(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
        NovoRestauranteDTO novo = new NovoRestauranteDTO(restaurante.getNome(), enderecoDTO, restaurante.getTipoCozinha().getCodigo(), restaurante.getHorarioAbertura(), restaurante.getHorarioFechamento(), restaurante.getDono().getId());

        RestauranteDTO restauranteCadastrado = this.dataSource.cadastrarRestaurante(novo);
        return this.mapearParaEntidade(restauranteCadastrado);

    }

    @Override
    public Restaurante atualizar(Restaurante restaurante) {
        Endereco endereco = restaurante.getEndereco();
        EnderecoDTO enderecoDTO = new EnderecoDTO(endereco.getId(), endereco.getLogradouro(), endereco.getNumero(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
        AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO(restaurante.getId(),
                restaurante.getNome(),
                enderecoDTO,
                restaurante.getTipoCozinha(),
                restaurante.getHorarioAbertura(),
                restaurante.getHorarioFechamento(),
                restaurante.getDono().getId(),
                null);
        RestauranteDTO restauranteAtualizado = this.dataSource.atualizarRestaurante(dto);
        return this.mapearParaEntidade(restauranteAtualizado);
    }

    private Restaurante mapearParaEntidade(RestauranteDTO dto) {
        Endereco endereco = Optional.ofNullable(dto.endereco())
                .map(e -> Endereco.restaurar(e.id(), e.logradouro(), e.numero(), e.cidade(), e.estado(), e.cep()))
                .orElse(null);

        Usuario usuarioDono = Optional.ofNullable(dto.dono())
                .map(this::mapearDono)
                .orElse(null);

        return Restaurante.restaurar(
                dto.id(),
                dto.nome(),
                dto.tipoCozinha(),
                dto.horarioInicio(),
                dto.horarioFim(),
                endereco,
                usuarioDono);
    }

    private Usuario mapearDono(UsuarioDonoDTO donoDto) {
        TipoUsuario tipoUsuario = Optional.ofNullable(donoDto.tipoUsuario())
                .map(tu -> TipoUsuario.restaurar(tu.id(), null, tu.nome(), tu.tipo()))
                .orElse(null);

        return Usuario.restaurar(donoDto.id(), donoDto.nome(), tipoUsuario);
    }

}
