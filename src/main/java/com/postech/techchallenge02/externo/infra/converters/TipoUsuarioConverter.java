package com.postech.techchallenge02.externo.infra.converters;

import com.postech.techchallenge02.core.enums.TipoUsuarioEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;

import jakarta.persistence.AttributeConverter;

public class TipoUsuarioConverter implements AttributeConverter<TipoUsuarioEnun, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TipoUsuarioEnun tipoUsuario) {
		if (tipoUsuario == null) {
			throw new RecursoNaoEncontradoException("Tipo usuario inexistente");
		}
		return tipoUsuario.getCodigo();
	}

	@Override
	public TipoUsuarioEnun convertToEntityAttribute(Integer codigoUsuario) {
		if (codigoUsuario == null) {
			throw new RecursoNaoEncontradoException("Tipo usuario inexistente");
		}
		return TipoUsuarioEnun.fromCodigo(codigoUsuario);
	}

}
