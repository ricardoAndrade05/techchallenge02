package com.postech.techchallenge02.core.gateways;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.dtos.Pagina;
import com.postech.techchallenge02.core.dtos.Paginacao;
import com.postech.techchallenge02.core.dtos.RestauranteDTO;
import com.postech.techchallenge02.core.dtos.TipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.interfaces.RestauranteDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;

public class TipoUsuarioGatewayImpl implements TipoUsuarioGateway {

    private final TipoUsuarioDataSource dataSource;
    private final RestauranteDataSource restauranteDataSource;
    private final UsuarioDataSource usuarioDataSource;

    private TipoUsuarioGatewayImpl(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource, UsuarioDataSource usuarioDataSource) {
        this.dataSource = dataSource;
        this.restauranteDataSource = restauranteDataSource;
        this.usuarioDataSource = usuarioDataSource;
    }

    public static TipoUsuarioGateway getInstance(TipoUsuarioDataSource dataSource, RestauranteDataSource restauranteDataSource  , UsuarioDataSource usuarioDataSource) {
        if (dataSource == null || restauranteDataSource == null || usuarioDataSource == null) {
            throw new IllegalArgumentException("Os data sources não podem ser nulos");
        }
        return new TipoUsuarioGatewayImpl(dataSource, restauranteDataSource, usuarioDataSource);
    }

    @Override
    public TipoUsuario atualizarTipoUsuario(TipoUsuario tipo) {
       RestauranteDTO restauranteDTO = new RestauranteDTO(tipo.getIdRestaurante(), tipo.getRestaurante().getNome());
       TipoUsuarioDTO dto = new TipoUsuarioDTO(tipo.getId(),restauranteDTO,tipo.getNome(),tipo.getTipo());
       dto = this.dataSource.atualizar(dto);
       return TipoUsuario.restaurar(dto.id(), tipo.getRestaurante(), dto.nome(), dto.tipo());
    }

    @Override
    public Optional<TipoUsuario> buscaTipoUsuarioPorId(Long idTipoUsuario) {
        TipoUsuarioDTO tipoUsuarioDTO = this.dataSource.buscaTipoUsuarioPorId(idTipoUsuario);
        if (tipoUsuarioDTO == null) {
            return Optional.empty();
        }

        RestauranteDTO restauranteDTO = tipoUsuarioDTO.restaurante();
        Restaurante restaurante = (restauranteDTO != null)
                ? Restaurante.restaurar(restauranteDTO.id(), restauranteDTO.nome())
                : null;

        TipoUsuario tipoUsuario = TipoUsuario.restaurar(
                idTipoUsuario,
                restaurante,
                tipoUsuarioDTO.nome(),
                tipoUsuarioDTO.tipo());

        return Optional.of(tipoUsuario);
    }

    @Override
    public TipoUsuario cadastraTipoUsuario(TipoUsuario tipoUsuario) {
        NovoTipoUsuarioDTO novoTipoUsuarioDTO = new NovoTipoUsuarioDTO(tipoUsuario.getRestaurante().getId(), tipoUsuario.getNome(), tipoUsuario.getTipo());
        TipoUsuarioDTO tipoUsuarioDTO = this.dataSource.cadastraTipoUsuario(novoTipoUsuarioDTO);
        return TipoUsuario.restaurar(tipoUsuarioDTO.id(), tipoUsuario.getRestaurante(), tipoUsuarioDTO.nome(), tipoUsuarioDTO.tipo());
    }

    @Override
    public boolean tipoUsuarioEstaVinculado(Long idTipoUsuario) {
        return this.usuarioDataSource.existeUsuarioComEsteTipoUsuario(idTipoUsuario);
    }

    @Override
    public void excluirTipoUsuario(Long idTipoUsuario) {
        this.dataSource.excluirTipoUsuario(idTipoUsuario);
    }

    @Override
    public Pagina<TipoUsuario> buscaTiposUsuarioPorRestaurante(Restaurante restaurante, Paginacao paginacao) {
        Pagina<TipoUsuarioDTO> pagina = this.dataSource.buscaTiposUsuarioPorRestaurante(restaurante.getId(),paginacao);
        List<TipoUsuarioDTO> tiposUsuarioDTO = pagina.itens();
        List<TipoUsuario> tipoUsuario = new ArrayList<>();
        for (TipoUsuarioDTO tipo : tiposUsuarioDTO) {
            tipoUsuario.add(TipoUsuario.restaurar(tipo.id(), restaurante, tipo.nome(), tipo.tipo()));
        }
        return new Pagina<>(tipoUsuario,paginacao.pagina(),paginacao.tamanho(),tipoUsuario.size(),tipoUsuario.size() / paginacao.tamanho());
    }

    @Override
    public boolean existeTipoUsuario(String nome, Long idRestaurante, TipoUsuarioEnun tipoUsuarioEnun) {
        return this.dataSource.existeTipoUsuario(nome,idRestaurante,tipoUsuarioEnun);   
    }

    @Override
    public boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante, TipoUsuarioEnun tipoUsuarioEnun) {
        return this.dataSource.existeTipoUsuario(idTipoUsuario,idRestaurante,tipoUsuarioEnun);
    }

}
