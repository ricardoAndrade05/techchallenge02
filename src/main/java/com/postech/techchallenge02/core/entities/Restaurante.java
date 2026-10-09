package com.postech.techchallenge02.core.entities;

public class Restaurante {

    private Long id;
    private String nome;

    public static Restaurante restaurar(Long id, String nome) {
        return new Restaurante(id, nome);
    }

    public static Restaurante criar(Long id, String nome) {
        return new Restaurante(id, nome);
    }

    private Restaurante(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

}
