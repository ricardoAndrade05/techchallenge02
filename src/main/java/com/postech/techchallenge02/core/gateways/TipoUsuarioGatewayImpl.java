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
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioDataSource;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioDataSource;

public class TipoUsuarioGatewayImpl implements TipoUsuarioGateway {

    private final TipoUsuarioDataSource dataSource;
    private final UsuarioDataSource usuarioDataSource;

    private TipoUsuarioGatewayImpl(TipoUsuarioDataSource dataSource, UsuarioDataSource usuarioDataSource) {
        this.dataSource = dataSource;
        this.usuarioDataSource = usuarioDataSource;
    }

    public static TipoUsuarioGateway getInstance(TipoUsuarioDataSource dataSource, UsuarioDataSource usuarioDataSource) {
        if (dataSource == null || usuarioDataSource == null) {
            throw new IllegalArgumentException("Os data sources não podem ser nulos");
        }
        return new TipoUsuarioGatewayImpl(dataSource, usuarioDataSource);
    }

    @Override
    public TipoUsuario atualizarTipoUsuario(TipoUsuario tipo) {
       Restaurante restaurante = tipo.getRestaurante();
       RestauranteDTO restauranteDTO = new RestauranteDTO(restaurante.getId(),restaurante.getNome(),null,null,null,null);
       TipoUsuarioDTO dto = new TipoUsuarioDTO(tipo.getId(),restauranteDTO,tipo.getNome(),tipo.getCategoria());
       dto = this.dataSource.atualizar(dto);
       return TipoUsuario.restaurar(dto.id(), restaurante, dto.nome(), dto.tipo());
    }

    @Override
    public Optional<TipoUsuario> buscaTipoUsuarioPorId(Long idTipoUsuario) {
        TipoUsuarioDTO tipoUsuarioDTO = this.dataSource.buscaTipoUsuarioPorId(idTipoUsuario);
        if (tipoUsuarioDTO == null) {
            return Optional.empty();
        }

        RestauranteDTO restauranteDTO = tipoUsuarioDTO.restaurante();
        Restaurante restaurante = (restauranteDTO != null)
                ? Restaurante.restaurar(restauranteDTO.id(), restauranteDTO.nome(),null,null,null,null,null)
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
        NovoTipoUsuarioDTO novoTipoUsuarioDTO = new NovoTipoUsuarioDTO(tipoUsuario.getRestaurante().getId(), tipoUsuario.getNome(), tipoUsuario.getCategoria());
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
    public boolean existeTipoUsuario(String nome, Long idRestaurante, CategoriaEnun tipoUsuarioEnun) {
        return this.dataSource.existeTipoUsuario(nome,idRestaurante,tipoUsuarioEnun);   
    }

    @Override
    public boolean existeTipoUsuario(Long idTipoUsuario, Long idRestaurante) {
        return this.dataSource.existeTipoUsuario(idTipoUsuario,idRestaurante);
    }

}
