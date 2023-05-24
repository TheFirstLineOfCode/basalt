package com.thefirstlineofcode.basalt.oxm.conversion;

public interface IConverter<K, V> {
	V from(K obj) throws ConversionException;
	K to(V obj) throws ConversionException;
}
