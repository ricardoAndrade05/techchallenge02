package com.postech.techchallenge02.infra.entities;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.infra.infra.converters.TipoUsuarioConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity 
@Table(name = "tb_tipo_usuario")
public class TipoUsuario {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Convert(converter = TipoUsuarioConverter.class)
    private TipoUsuarioEnun tipo;
    
    @ManyToOne
    @JoinColumn(name="id_restaurante")
    private Restaurante restaurante;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoUsuarioEnun getTipo() {
        return tipo;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(TipoUsuarioEnun tipo) {
        this.tipo = tipo;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
    

    
}
