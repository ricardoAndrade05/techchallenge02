package com.postech.techchallenge02.core.enums;

import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;

public enum DisponibilidadeCardapioEnum {

	TODOS(1, "Disponível para consumo, retirada e delivery"),
	LOCAL(2, "Consumo no local"),
	RETIRADA(3, "Retirada no restaurante"),
	ENTREGA(4, "Entrega de delivery");

	private final int codigo;
	private final String descricao;

	DisponibilidadeCardapioEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static DisponibilidadeCardapioEnum fromCodigo(int codigo) {
		for (DisponibilidadeCardapioEnum tipo : DisponibilidadeCardapioEnum.values()) {
			if (tipo.getCodigo() == codigo) {
				return tipo;
			}
		}
		throw new RecursoNaoEncontradoException("Código de disponibilidade do cardapio inválido: " + codigo);
	}
}
