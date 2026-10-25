package com.postech.techchallenge02.core.entities;

import java.util.Objects;

import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

public class TipoUsuario {

    private Long id;
    private String nome;
    private Restaurante restaurante;
    private CategoriaEnun categoria;

    public static TipoUsuario criar(Restaurante restaurante, String nome, CategoriaEnun tipo) {
        return new TipoUsuario(null, restaurante, nome, tipo);
    }

    public static TipoUsuario restaurar(Long id, Restaurante restaurante, String nome, CategoriaEnun tipo) {
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.id = id;
        tipoUsuario.restaurante = restaurante;
        tipoUsuario.nome = nome;
        tipoUsuario.categoria = tipo;
        return tipoUsuario;
    }

    public void atualizar(String novoNomeTipo) {
        setNome(novoNomeTipo);
        setCategoria(categoria);
    }

    public Long getId() {
        return id;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Long getIdRestaurante() {
        return restaurante != null ? restaurante.getId() : null;
    }

    public String getNome() {
        return nome;
    }

    public CategoriaEnun getCategoria() {
        return categoria;
    }

    private TipoUsuario(){
    }

    private TipoUsuario(Long id, Restaurante restaurante, String nome, CategoriaEnun tipo) {
        this.id = id;
        setRestaurante(restaurante);
        setNome(nome);
        setCategoria(tipo);
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

    private void setCategoria(CategoriaEnun categoria) {
        if (categoria == null) {
            throw new DadosInvalidosException("Tipo de usuário inválido");
        }
        this.categoria = categoria;
    }

}
