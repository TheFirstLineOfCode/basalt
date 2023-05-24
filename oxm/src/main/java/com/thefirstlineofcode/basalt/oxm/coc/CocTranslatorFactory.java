package com.thefirstlineofcode.basalt.oxm.coc;

import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;

public class CocTranslatorFactory<T> implements ITranslatorFactory<T> {
	private Class<T> type;
	private CocTranslator<T> translator;
	
	public CocTranslatorFactory(Class<T> type) {
		this.type = type;
		translator = new CocTranslator<>(type);
	}

	@Override
	public ITranslator<T> create() {
		return translator;
	}

	@Override
	public Class<T> getType() {
		return type;
	}

}
