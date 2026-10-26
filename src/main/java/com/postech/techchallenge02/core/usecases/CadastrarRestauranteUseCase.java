package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.EnderecoDTO;
import com.postech.techchallenge02.core.dtos.NovoRestauranteDTO;
import com.postech.techchallenge02.core.entities.Endereco;
import com.postech.techchallenge02.core.entities.Restaurante;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.interfaces.RestauranteGateway;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class CadastrarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static CadastrarRestauranteUseCase getInstance(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        return new CadastrarRestauranteUseCase(restauranteGateway, usuarioGateway, tipoUsuarioGateway);
    }

    public Restaurante execute(NovoRestauranteDTO dto) {
        Usuario usuario = this.usuarioGateway.buscaUsuarioPorId(dto.idUsuarioLogado()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
        EnderecoDTO enderecoDTO = dto.endereco();
        Endereco endereco = Endereco.criar(enderecoDTO.logradouro(), enderecoDTO.numero(),enderecoDTO.cidade(),enderecoDTO.estado(),enderecoDTO.cep());
        TipoCozinhaEnun tipoCozinha = TipoCozinhaEnun.fromCodigo(dto.tipoCozinha());
        
        Restaurante restaurante = Restaurante.criar(dto.nome(), tipoCozinha, dto.horarioAbertura(), dto.horarioFechamento(), endereco, usuario);        
        restaurante = this.restauranteGateway.cadastrar(restaurante);

        TipoUsuario tipoUsuario = TipoUsuario.criar(restaurante, "Proprietário", CategoriaEnun.DONO);
        tipoUsuario = this.tipoUsuarioGateway.cadastraTipoUsuario(tipoUsuario);

        this.usuarioGateway.vincularUsuarioAoTipo(usuario.getId(), tipoUsuario.getId());

        return this.restauranteGateway.buscaRestaurantePorId(restaurante.getId()).orElseThrow(() -> new RecursoNaoEncontradoException("Erro ao recuperar o restaurante cadastrado"));
    }

    private CadastrarRestauranteUseCase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

}
