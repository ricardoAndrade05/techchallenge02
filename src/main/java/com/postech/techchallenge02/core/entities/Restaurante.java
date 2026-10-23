package com.postech.techchallenge02.core.entities;

import java.time.LocalTime;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.exceptions.DadosInvalidosException;

public class Restaurante {

    private Long id;
    private String nome;
    private TipoCozinhaEnun tipoCozinha;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    Endereco endereco;
    Usuario dono;

    public static Restaurante restaurar(Long id, String nome, TipoCozinhaEnun tipoCozinha, LocalTime horarioAbertura, LocalTime horarioFechamento, Endereco endereco, Usuario dono) {
        return new Restaurante(id, nome, tipoCozinha, horarioAbertura, horarioFechamento, endereco, dono);
    }

    public static Restaurante criar(String nome, TipoCozinhaEnun tipoCozinha, LocalTime horarioAbertura, LocalTime horarioFechamento, Endereco endereco, Usuario dono) {
        return new Restaurante(nome, tipoCozinha, horarioAbertura, horarioFechamento, endereco, dono);
    }

    public void atualizar(String nome, TipoCozinhaEnun tipoCozinha, LocalTime horaAbertura, LocalTime horaFechamento, Usuario usuarioDono) {
        if (nome != null && !nome.isBlank()) {
            this.setNome(nome);
        }
        if (tipoCozinha != null) {
            this.setTipoCozinha(tipoCozinha);
        }
        LocalTime abertura = horaAbertura != null ? horaAbertura : this.horarioAbertura;
        LocalTime fechamento = horaFechamento != null ? horaFechamento : this.horarioFechamento;

        this.validaHorarioFuncionamento(abertura, fechamento);

        if (usuarioDono != null) {
            this.setDono(usuarioDono);
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoCozinhaEnun getTipoCozinha() {
        return tipoCozinha;
    }

    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }

    public LocalTime getHorarioFechamento() {
        return horarioFechamento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Usuario getDono() {
        return dono;
    }

    private Restaurante(Long id, String nome, TipoCozinhaEnun tipoCozinha, LocalTime horarioAbertura, LocalTime horarioFechamento, Endereco endereco, Usuario dono) {
        this.id = id;
        this.nome = nome;
        this.tipoCozinha = tipoCozinha;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.endereco = endereco;
        this.dono = dono;
    }

    private Restaurante(String nome, TipoCozinhaEnun tipoCozinha, LocalTime horarioAbertura, LocalTime horarioFechamento, Endereco endereco, Usuario dono) {
        this.id = null;
        this.setNome(nome);
        this.setTipoCozinha(tipoCozinha);
        this.validaHorarioFuncionamento(horarioAbertura, horarioFechamento);
        this.setEndereco(endereco);
        this.setDono(dono);
    }

    private void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("O nome não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();
    }

    private void setTipoCozinha(TipoCozinhaEnun tipoCozinha) {
        if (tipoCozinha == null) {
            throw new DadosInvalidosException("O tipo de cozinha não pode ser nulo");
        }
        this.tipoCozinha = tipoCozinha;
    }

    private void setHorarioAbertura(LocalTime horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    private void setHorarioFechamento(LocalTime horarioFechamento) {

        this.horarioFechamento = horarioFechamento;
    }

    private void setEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço não pode ser nulo");
        }
        this.endereco = endereco;
    }

    private void setDono(Usuario dono) {
        if (dono == null) {
            throw new IllegalArgumentException("O dono não pode ser nulo");
        }
        this.dono = dono;
    }

    private void validaHorarioFuncionamento(LocalTime horarioAbertura, LocalTime horarioFechamento) {
        if (horarioAbertura == null) {
            throw new DadosInvalidosException("O horário de abertura não pode ser nulo.");
        }
        if (horarioFechamento == null) {
            throw new DadosInvalidosException("O horário de fechamento não pode ser nulo.");
        }
        if (horarioAbertura.isAfter(horarioFechamento)) {
            throw new DadosInvalidosException("O horário de abertura deve ser anterior que o horário de fechamento.");
        }
        if (horarioAbertura.equals(horarioFechamento)) {
            throw new DadosInvalidosException("Os horarios de abertura/fechamento devem ser diferentes.");
        }
        this.setHorarioAbertura(horarioAbertura);
        this.setHorarioFechamento(horarioFechamento);
    }

}
