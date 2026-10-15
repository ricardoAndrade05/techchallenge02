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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
        TipoUsuario other = (TipoUsuario) obj;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (tipo != other.tipo)
            return false;
        if (restaurante == null) {
            if (other.restaurante != null)
                return false;
        } else if (!restaurante.equals(other.restaurante))
            return false;
        return true;
    }
    
    
    
}
