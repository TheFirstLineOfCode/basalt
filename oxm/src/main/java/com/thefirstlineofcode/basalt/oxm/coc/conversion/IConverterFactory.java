package com.thefirstlineofcode.basalt.oxm.coc.conversion;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;

public interface IConverterFactory {
	IConverter<?, ?> create(Annotation annotation);
}
