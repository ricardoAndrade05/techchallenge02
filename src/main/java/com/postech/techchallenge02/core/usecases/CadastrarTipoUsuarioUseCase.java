package com.postech.techchallenge02.core.usecases;

import com.postech.techchallenge02.core.dtos.NovoTipoUsuarioDTO;
import com.postech.techchallenge02.core.entities.TipoUsuario;
import com.postech.techchallenge02.core.exceptions.TipoUsuarioExistenteException;
import com.postech.techchallenge02.core.interfaces.TipoUsuarioGateway;

public class CadastrarTipoUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public static CadastrarTipoUsuarioUseCase getInstance(TipoUsuarioGateway tipoUsuarioGateway) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuarioGateway);
    }

    public TipoUsuario execute(NovoTipoUsuarioDTO novoTipoUsuarioDTO) {
        final boolean existe = this.tipoUsuarioGateway.existeTipoUsuario(novoTipoUsuarioDTO);
        if (existe) {
            throw new TipoUsuarioExistenteException("Tipo " + novoTipoUsuarioDTO.nomeTipo() + " já cadastrado");
        }

        final TipoUsuario novoTipoUsuario = TipoUsuario.criar(novoTipoUsuarioDTO.restaurante(),novoTipoUsuarioDTO.nomeTipo(),novoTipoUsuarioDTO.tipoUsuario());

        return this.tipoUsuarioGateway.cadastraTipoUsuario(novoTipoUsuario);
    }

    private CadastrarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

}
