package com.thefirstlineofcode.basalt.oxm.coc.conversion;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;

public class ConversionService {
	private static IConversionFactory conversionFactory = new ConversionFactory();
	
	@SuppressWarnings("unchecked")
	public static <K, V> V from(Annotation annotation, K obj) throws ConversionException {
		return (V)getConveter(annotation).from(obj);
	}

	@SuppressWarnings("unchecked")
	private static <K, V> IConverter<K, V> getConveter(Annotation annotation) throws ConversionException {
		return (IConverter<K, V>)conversionFactory.getConverter(annotation);
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> K to(Annotation annotation, V obj) throws ConversionException {
		return (K)getConveter(annotation).to(obj);
	}
}
