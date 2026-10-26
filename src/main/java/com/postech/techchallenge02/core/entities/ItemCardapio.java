package com.postech.techchallenge02.core.entities;

import java.util.Objects;

import com.postech.techchallenge02.core.enums.DisponibilidadeCardapioEnum;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

public class ItemCardapio {
	private Long id;

	private String nome;

	private String descricao;

	private Float preco;

	private DisponibilidadeCardapioEnum tipoDisponibilidade;

	private String caminhoImagem;

	private Restaurante restaurante;

	public static ItemCardapio cria(String nome, String descricao, Float preco,
			DisponibilidadeCardapioEnum tipoDisponibilidade,
			String caminhoImagem, Restaurante restaurante) {
		return new ItemCardapio(nome, descricao, preco, tipoDisponibilidade, caminhoImagem, restaurante);
	}

	public static ItemCardapio restaura(Long id, String nome, String descricao, Float preco,
			DisponibilidadeCardapioEnum tipoDisponibilidade,
			String caminhoImagem, Restaurante restaurante) {
		return new ItemCardapio(id, nome, descricao, preco, tipoDisponibilidade, caminhoImagem, restaurante);
	}

	private ItemCardapio(Long id, String nome, String descricao, Float preco,
			DisponibilidadeCardapioEnum tipoDisponibilidade,
			String caminhoImagem, Restaurante restaurante) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.tipoDisponibilidade = tipoDisponibilidade;
		this.caminhoImagem = caminhoImagem;
		this.restaurante = restaurante;
	}

	private ItemCardapio(String nome, String descricao, Float preco, DisponibilidadeCardapioEnum tipoDisponibilidade,
			String caminhoImagem, Restaurante restaurante) {
		this.setNome(nome);
		this.setPreco(preco);
		this.setTipoDisponibilidade(tipoDisponibilidade);
		this.setRestaurante(restaurante);
		this.descricao = descricao;
		this.caminhoImagem = caminhoImagem;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public Float getPreco() {
		return preco;
	}

	public DisponibilidadeCardapioEnum getTipoDisponibilidade() {
		return tipoDisponibilidade;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public Long getIdRestaurante() {
		return restaurante != null ? restaurante.getId() : null;
	}

	private void setNome(String nome) {
		if (Objects.isNull(nome) || nome.isBlank()) {
			throw new DadosInvalidosException("Nome inválido");
		}
		this.nome = nome;
	}

	private void setRestaurante(Restaurante restaurante) {
		if (restaurante == null) {
			throw new DadosInvalidosException("Restaurante inválido");
		}
		this.restaurante = restaurante;
	}

	private void setTipoDisponibilidade(DisponibilidadeCardapioEnum tipoDisponibilidade) {
		if (tipoDisponibilidade == null) {
			throw new DadosInvalidosException("Tipo de disponibilidade inválido");
		}
		this.tipoDisponibilidade = tipoDisponibilidade;
	}

	private void setPreco(Float preco) {
		if (preco == null || preco <= 0) {
			throw new DadosInvalidosException("Preço inválido");
		}
		this.preco = preco;
	}
}
