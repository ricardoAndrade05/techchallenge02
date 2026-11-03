package com.postech.techchallenge02.infra.infra.converters;

import com.postech.techchallenge02.core.enums.DisponibilidadeCardapioEnum;
import com.postech.techchallenge02.core.exceptions.RecursoNaoEncontradoException;

import jakarta.persistence.AttributeConverter;

public class DisponibilidadeCardapioConverter  implements AttributeConverter<DisponibilidadeCardapioEnum, Integer> {

	@Override
	public Integer convertToDatabaseColumn(DisponibilidadeCardapioEnum tipoDisponibilidade) {
		if (tipoDisponibilidade == null) {
			throw new RecursoNaoEncontradoException("Tipo de disponibilidade inexistente");
		}
		return tipoDisponibilidade.getCodigo();
	}

	@Override
	public DisponibilidadeCardapioEnum convertToEntityAttribute(Integer codigo) {
		if (codigo == null) {
			throw new RecursoNaoEncontradoException("Tipo de disponibilidade inexistente");
		}
		return DisponibilidadeCardapioEnum.fromCodigo(codigo);
	}
    
}
