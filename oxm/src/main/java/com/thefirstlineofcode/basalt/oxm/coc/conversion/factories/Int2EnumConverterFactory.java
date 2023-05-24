package com.thefirstlineofcode.basalt.oxm.coc.conversion.factories;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.IConverterFactory;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.Int2Enum;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;
import com.thefirstlineofcode.basalt.oxm.conversion.converters.Int2EnumConverter;

public class Int2EnumConverterFactory implements IConverterFactory {
	@Override
	public IConverter<String, Object> create(Annotation annotation) {
		Int2Enum int2Enum = (Int2Enum)annotation;
		return new Int2EnumConverter(int2Enum.value());
	}
}
