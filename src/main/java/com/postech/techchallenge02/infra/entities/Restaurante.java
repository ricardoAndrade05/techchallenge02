package com.postech.techchallenge02.infra.entities;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.infra.infra.converters.TipoCozinhaConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_restaurante")

public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalTime horarioFuncionamentoInicio;
    private LocalTime horarioFuncionamentoFim;

    @Convert(converter = TipoCozinhaConverter.class)
    private TipoCozinhaEnun tipoCozinha;

    @ManyToOne
    @JoinColumn(name = "id_usuario_dono")
    private Usuario dono;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco") 
    private Endereco endereco;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoUsuario> tiposUsuario = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public LocalTime getHorarioFuncionamentoInicio() {
        return horarioFuncionamentoInicio;
    }
    public LocalTime getHorarioFuncionamentoFim() {
        return horarioFuncionamentoFim;
    }
    public TipoCozinhaEnun getTipoCozinha() {
        return tipoCozinha;
    }
    public Usuario getDono() {
        return dono;
    }
    public Endereco getEndereco() {
        return endereco;
    }
    public List<TipoUsuario> getTiposUsuario() {
        return tiposUsuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setHorarioFuncionamentoInicio(LocalTime horarioFuncionamentoInicio) {
        this.horarioFuncionamentoInicio = horarioFuncionamentoInicio;
    }

    public void setHorarioFuncionamentoFim(LocalTime horarioFuncionamentoFim) {
        this.horarioFuncionamentoFim = horarioFuncionamentoFim;
    }

    public void setTipoCozinha(TipoCozinhaEnun tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setTiposUsuario(List<TipoUsuario> tiposUsuario) {
        this.tiposUsuario = tiposUsuario;
    }

}
