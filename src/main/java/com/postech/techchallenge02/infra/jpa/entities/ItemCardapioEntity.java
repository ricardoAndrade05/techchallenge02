package com.postech.techchallenge02.infra.jpa.entities;

import com.postech.techchallenge02.core.enums.DisponibilidadeCardapioEnum;
import com.postech.techchallenge02.infra.infra.converters.DisponibilidadeCardapioConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_item_cardapio")
public class ItemCardapioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String descricao;

	private Float preco;

	private String caminhoImagem;

	@Convert(converter = DisponibilidadeCardapioConverter.class)
	private DisponibilidadeCardapioEnum tipoDisponibilidade;

	@ManyToOne
	@JoinColumn(name = "id_restaurante")
	private RestauranteEntity restaurante;

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public DisponibilidadeCardapioEnum getTipoDisponibilidade() {
		return tipoDisponibilidade;
	}

	public RestauranteEntity getRestaurante() {
		return restaurante;
	}

	public Float getPreco() {
		return preco;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setTipoDisponibilidade(DisponibilidadeCardapioEnum tipoDisponibilidade) {
		this.tipoDisponibilidade = tipoDisponibilidade;
	}

	public void setRestaurante(RestauranteEntity restaurante) {
		this.restaurante = restaurante;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}

	public void setPreco(Float preco) {
		this.preco = preco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((tipoDisponibilidade == null) ? 0 : tipoDisponibilidade.hashCode());
		result = prime * result + ((restaurante == null) ? 0 : restaurante.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemCardapioEntity other = (ItemCardapioEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (tipoDisponibilidade != other.tipoDisponibilidade)
			return false;
		if (restaurante == null) {
			if (other.restaurante != null)
				return false;
		} else if (!restaurante.equals(other.restaurante))
			return false;
		return true;
	}

}
