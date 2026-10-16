package com.postech.techchallenge02.core.entities;

import java.util.Objects;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

public class TipoUsuario {

    private Long id;
    private String nome;
    private Restaurante restaurante;
    private TipoUsuarioEnun tipo;

    public static TipoUsuario criar(Restaurante restaurante, String nome, TipoUsuarioEnun tipo) {
        return new TipoUsuario(null, restaurante, nome, tipo);
    }

    public static TipoUsuario restaurar(Long id, Restaurante restaurante, String nome, TipoUsuarioEnun tipo) {
        return new TipoUsuario(id, restaurante, nome, tipo);
    }

    public void atualizar(String novoNomeTipo) {
        setNome(novoNomeTipo);
        setTipo(tipo);
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

    public TipoUsuarioEnun getTipo() {
        return tipo;
    }

    private TipoUsuario(Long id, Restaurante restaurante, String nome, TipoUsuarioEnun tipo) {
        this.id = id;
        setRestaurante(restaurante);
        setNome(nome);
        setTipo(tipo);
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

    private void setTipo(TipoUsuarioEnun tipo) {
        if (tipo == null) {
            throw new DadosInvalidosException("Tipo de usuário inválido");
        }
        this.tipo = tipo;
    }

}
