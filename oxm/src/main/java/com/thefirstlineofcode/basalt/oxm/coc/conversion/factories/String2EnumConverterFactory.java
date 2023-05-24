package com.thefirstlineofcode.basalt.oxm.coc.conversion.factories;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.IConverterFactory;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2Enum;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;
import com.thefirstlineofcode.basalt.oxm.conversion.converters.String2EnumConverter;

public class String2EnumConverterFactory implements IConverterFactory {
	
	@Override
	public IConverter<String, Object> create(Annotation annotation) {
		String2Enum string2Enum = (String2Enum)annotation;
		return new String2EnumConverter(string2Enum.value(), string2Enum.upperCaseString(),
				string2Enum.dashToUnderline());
	}
	
}
