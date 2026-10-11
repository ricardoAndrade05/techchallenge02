package com.postech.techchallenge02.core.entities;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;

public class Usuario {

    private Long id;
    private String nome;
    private TipoUsuario tipoUsuario;

    public static Usuario restaurar(Long id, String nome, TipoUsuario tipoUsuario) {
        return new Usuario(id, nome, tipoUsuario);
    }

    public boolean podeCadastrarTipoUsuario() {
        return this.tipoUsuario != null && !TipoUsuarioEnun.CLIENTE.equals(this.tipoUsuario.getTipo());
    }

    public boolean podeExcluirTipoUsuario() {
        return this.tipoUsuario != null && !TipoUsuarioEnun.CLIENTE.equals(this.tipoUsuario.getTipo());
    }

    public boolean podeAtualizarTipoUsuario() {
        return this.tipoUsuario != null && !TipoUsuarioEnun.CLIENTE.equals(this.tipoUsuario.getTipo());
    }

    private Usuario(Long id, String nome, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Usuario other = (Usuario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

}
