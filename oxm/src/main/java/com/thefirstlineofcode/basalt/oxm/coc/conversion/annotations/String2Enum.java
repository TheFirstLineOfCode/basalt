package com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.ConverterFactory;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.factories.String2EnumConverterFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ConverterFactory(String2EnumConverterFactory.class)
public @interface String2Enum {
	Class<?> value();
	boolean upperCaseString() default true;
	boolean dashToUnderline() default true;
}
