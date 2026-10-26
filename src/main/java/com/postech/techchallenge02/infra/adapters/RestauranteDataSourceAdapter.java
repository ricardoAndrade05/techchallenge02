package com.postech.techchallenge02.infra.adapters;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.postech.techchallenge02.core.dtos.AtualizarRestauranteDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.infra.entities.Restaurante;
import com.postech.techchallenge02.infra.entities.TipoUsuario;
import com.postech.techchallenge02.infra.entities.Usuario;
import com.postech.techchallenge02.infra.infra.mappers.RestauranteMapper;
import com.postech.techchallenge02.infra.repositories.RestauranteRepository;
import com.postech.techchallenge02.infra.repositories.UsuarioRepository;

@Component
public class RestauranteDataSourceAdapter implements RestauranteDataSource {

    private final RestauranteRepository restauranteRepository;
    private final RestauranteMapper restauranteMapper;
    private final UsuarioRepository usuarioRepository;

    public RestauranteDataSourceAdapter(RestauranteRepository restauranteRepository, RestauranteMapper restauranteMapper, UsuarioRepository usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteMapper = restauranteMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public RestauranteDTO buscaRestaurantePorId(Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = this.restauranteRepository.findById(idRestaurante);
        if (restauranteOptional.isEmpty()) {
            return null;
        }

        RestauranteDTO dto = restauranteMapper.toDTO(restauranteOptional.get());
        return dto;
    }

    @Override
    public void excluirRestaurante(Long idRestaurante) {
        Restaurante restaurante = restauranteRepository.findById(idRestaurante).get();
        List<TipoUsuario> tiposDoRestaurante = restaurante.getTiposUsuario();
        if (!tiposDoRestaurante.isEmpty()) {
            Set<Long> idsParaRemover = tiposDoRestaurante.stream().map(TipoUsuario::getId).collect(Collectors.toSet());
            List<Usuario> usuarios = usuarioRepository.findAllByTiposUsuarioIn(tiposDoRestaurante);
            usuarios.forEach(usuario -> usuario.getTiposUsuario().removeIf(tu -> idsParaRemover.contains(tu.getId())));
            usuarioRepository.saveAll(usuarios);
            usuarioRepository.flush();
        }
        restauranteRepository.delete(restaurante);
    }

    @Override
    public RestauranteDTO cadastrarRestaurante(NovoRestauranteDTO dto) {
        Restaurante restaurante = restauranteMapper.toEntity(dto);
        Usuario dono = usuarioRepository.findById(dto.idUsuarioLogado()).get();
        restaurante.setDono(dono);

        restaurante = restauranteRepository.saveAndFlush(restaurante);

        return restauranteMapper.toDTO(restaurante);

    }

@Override
public RestauranteDTO atualizarRestaurante(AtualizarRestauranteDTO dto) {
    Restaurante restaurante = restauranteRepository.findById(dto.idRestaurante()).get();
    restaurante.setNome(dto.nome());
    restaurante.setTipoCozinha(dto.tipoCozinha());
    restaurante.setHorarioFuncionamentoInicio(dto.horaAbertura());
    restaurante.setHorarioFuncionamentoFim(dto.horaFechamento());
    if (dto.endereco() != null && restaurante.getEndereco() != null) {
        restaurante.getEndereco().setLogradouro(dto.endereco().logradouro());
        restaurante.getEndereco().setNumero(dto.endereco().numero());
        restaurante.getEndereco().setCidade(dto.endereco().cidade());
        restaurante.getEndereco().setEstado(dto.endereco().estado());
        restaurante.getEndereco().setCep(dto.endereco().cep());
    }
    Usuario dono = usuarioRepository.findById(dto.idDono()).get();
    restaurante.setDono(dono);
    Restaurante restauranteAtualizado = restauranteRepository.saveAndFlush(restaurante);
    return restauranteMapper.toDTO(restauranteAtualizado);
}

}
