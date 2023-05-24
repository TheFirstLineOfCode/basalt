package com.thefirstlineofcode.basalt.oxm.translating;

public interface ITranslatorFactory<T> {
	Class<T> getType();
	ITranslator<T> create();
}
