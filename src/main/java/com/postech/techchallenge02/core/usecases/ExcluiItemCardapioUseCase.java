package com.postech.techchallenge02.core.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.postech.techchallenge02.core.dtos.ExclusaoItemCardapioDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;
import com.postech.techchallenge02.core.entities.Usuario;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;
import com.postech.techchallenge02.core.exceptions.UsuarioSemPermissaoException;
import com.postech.techchallenge02.core.interfaces.ItemCardapioGateway;
import com.postech.techchallenge02.core.interfaces.UsuarioGateway;

public class ExcluiItemCardapioUseCase {

	private final ItemCardapioGateway cardapioGateway;
	private final UsuarioGateway usuarioGateway;

	public static ExcluiItemCardapioUseCase getInstance(ItemCardapioGateway cardapioGateway,
			UsuarioGateway usuarioGateway) {
		return new ExcluiItemCardapioUseCase(cardapioGateway, usuarioGateway);
	}

	private ExcluiItemCardapioUseCase(ItemCardapioGateway cardapioGateway, UsuarioGateway usuarioGateway) {
		this.cardapioGateway = cardapioGateway;
		this.usuarioGateway = usuarioGateway;
	}

	public void execute(ExclusaoItemCardapioDTO dto) {
		this.validaDadosEntrada(dto);

		ItemCardapio itemCardapioDomain = this.cardapioGateway.consulta(dto.idItemCardapio())
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Item Cardapio não encontrado/inexistente: " + dto.idItemCardapio()));

		Usuario usuarioLogado = this.usuarioGateway
				.buscaUsuarioPorId(dto.idUsuarioLogado(), itemCardapioDomain.getIdRestaurante())
				.orElseThrow(() -> new RecursoNaoEncontradoException(
						"Usuário não encontrado/inexistente para esse restaurante."));

		if (!usuarioLogado.podeExcluir()) {
			throw new UsuarioSemPermissaoException(
					"Usuário " + dto.idUsuarioLogado() + " não tem permissão para excluir itens do cardápio");
		}
		this.cardapioGateway.exclusaoItem(itemCardapioDomain.getId());
	}

	private void validaDadosEntrada(ExclusaoItemCardapioDTO dto) {
		List<String> erros = new ArrayList<>();

		if (Objects.isNull(dto.idUsuarioLogado())) {
			erros.add("Usuário inválido");
		}
		if (Objects.isNull(dto.idItemCardapio())) {
			erros.add("Item do cardapio não pode ser nulo");
		}
		if (!erros.isEmpty()) {
			throw new DadosInvalidosException(String.join(" ", erros));
		}
	}

}
