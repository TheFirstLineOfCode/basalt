package com.thefirstlineofcode.basalt.oxm.coc.conversion;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;

public interface IConversionFactory {
	<T extends Annotation> IConverter<?, ?> getConverter(T annotation);
}
