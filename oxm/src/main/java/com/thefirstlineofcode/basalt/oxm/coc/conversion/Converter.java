package com.thefirstlineofcode.basalt.oxm.coc.conversion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Converter {
	Class<? extends IConverter<?, ?>> value();
}
