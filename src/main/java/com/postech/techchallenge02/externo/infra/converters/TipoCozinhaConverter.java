package com.postech.techchallenge02.externo.infra.converters;

import com.postech.techchallenge02.core.enums.TipoCozinhaEnun;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;

import jakarta.persistence.AttributeConverter;

public class TipoCozinhaConverter implements AttributeConverter<TipoCozinhaEnun, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TipoCozinhaEnun tipoUsuario) {
		if (tipoUsuario == null) {
			throw new RecursoNaoEncontradoException("Tipo usuario inexistente");
		}
		return tipoUsuario.getCodigo();
	}

	@Override
	public TipoCozinhaEnun convertToEntityAttribute(Integer codigoUsuario) {
		if (codigoUsuario == null) {
			throw new RecursoNaoEncontradoException("Tipo usuario inexistente");
		}
		return TipoCozinhaEnun.fromCodigo(codigoUsuario);
	}

}
