package com.postech.techchallenge02.core.entities;

import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

public class Endereco {

    private Long id;
    private String logradouro;
    private String numero;
    private String cidade;
    private String estado;
    private String cep;

    public static Endereco restaurar(Long id, String logradouro, String numero, String cidade, String estado, String cep) {
        return new Endereco(id, logradouro, numero, cidade, estado, cep);
    }

    public static Endereco criar(String logradouro, String numero, String cidade, String estado, String cep) {
        return new Endereco(logradouro, numero, cidade, estado, cep);
    }

    public void atualizar(String logradouro, String numero, String cidade, String estado, String cep) {
        if (logradouro != null && !logradouro.isBlank()) {
            this.setLogradouro(logradouro);
        }
        if (numero != null && !numero.isBlank()) {
            this.setNumero(numero);
        }
        if (cidade != null && !cidade.isBlank()) {
            this.setCidade(cidade);
        }
        if (estado != null && !estado.isBlank()) {
            this.setEstado(estado);
        }
        if (cep != null && !cep.isBlank()) {
            this.setCep(cep);
        }
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

    public void setLogradouro(String logradouro) {
        if (logradouro.isBlank()) {
            throw new DadosInvalidosException("O logradouro não pode ser nulo ou vazio");
        }
        this.logradouro = logradouro;
    }

    public void setNumero(String numero) {
        if (numero.isBlank()) {
            throw new DadosInvalidosException("O número não pode ser nulo ou vazio");
        }
        this.numero = numero;
    }

    public void setCidade(String cidade) {
        if (cidade.isBlank()) {
            throw new DadosInvalidosException("A cidade não pode ser nula ou vazia");
        }
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        if (estado.isBlank()) {
            throw new DadosInvalidosException("O estado não pode ser nulo ou vazio");
        }
        this.estado = estado;
    }

    public void setCep(String cep) {
        if (cep.isBlank()) {
            throw new DadosInvalidosException("O cep não pode ser nulo ou vazio");
        }
        this.cep = cep;
    }

    private Endereco(Long id, String logradouro, String numero, String cidade, String estado, String cep) {
        this.id = id;
        this.logradouro = logradouro;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    private Endereco(String logradouro, String numero, String cidade, String estado, String cep) {
        this.id = null;
        this.setLogradouro(logradouro);
        this.setNumero(numero);
        this.setCidade(cidade);
        this.setEstado(estado);
        this.setCep(cep);
    }

}
