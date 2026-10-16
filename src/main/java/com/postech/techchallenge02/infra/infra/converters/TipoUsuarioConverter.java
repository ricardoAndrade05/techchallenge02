package com.postech.techchallenge02.infra.infra.converters;

import com.postech.techchallenge02.core.enums.CategoriaEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;

import jakarta.persistence.AttributeConverter;

public class TipoUsuarioConverter implements AttributeConverter<CategoriaEnun, Integer> {

	@Override
	public Integer convertToDatabaseColumn(CategoriaEnun tipoUsuario) {
		if (tipoUsuario == null) {
			throw new RecursoNaoEncontradoException("Tipo usuario inexistente");
		}
		return tipoUsuario.getCodigo();
	}

	@Override
	public CategoriaEnun convertToEntityAttribute(Integer codigoUsuario) {
		if (codigoUsuario == null) {
			throw new RecursoNaoEncontradoException("Tipo usuario inexistente");
		}
		return CategoriaEnun.fromCodigo(codigoUsuario);
	}

}
