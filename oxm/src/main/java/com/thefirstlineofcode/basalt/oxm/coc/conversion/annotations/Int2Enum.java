package com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.ConverterFactory;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.factories.Int2EnumConverterFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ConverterFactory(Int2EnumConverterFactory.class)
public @interface Int2Enum {
	Class<?> value();
}
