package com.postech.techchallenge02.core.interfaces;

import com.postech.techchallenge02.core.dtos.ItemCardapioDTO;
import com.postech.techchallenge02.core.entities.ItemCardapio;

public interface ItemCardapioDataSource {
  
	ItemCardapioDTO salva(ItemCardapio itemCardapioDomain);
}
