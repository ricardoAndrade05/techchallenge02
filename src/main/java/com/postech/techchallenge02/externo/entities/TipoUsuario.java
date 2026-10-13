package com.postech.techchallenge02.externo.entities;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.externo.infra.converters.TipoUsuarioConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity 
@Table(name = "tb_tipo_usuario")
@Getter 
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
    
}
