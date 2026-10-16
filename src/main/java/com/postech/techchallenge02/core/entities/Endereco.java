package com.postech.techchallenge02.core.entities;

public class Endereco {

    private Long id;
    private String logradouro;
    private String numero;
    private String cidade;
    private String estado;
    private String cep;

    public static Endereco restaurar(Long id, String logradouro, String numero, String cidade, String estado, String cep){
        return new Endereco(id, logradouro, numero, cidade, estado, cep);
    }

    public static Endereco criar(String logradouro, String numero, String cidade, String estado, String cep){
        return new Endereco(null, logradouro, numero, cidade, estado, cep);
    }

    private Endereco(Long id, String logradouro, String numero, String cidade, String estado, String cep) {
        this.id = id;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    

}
