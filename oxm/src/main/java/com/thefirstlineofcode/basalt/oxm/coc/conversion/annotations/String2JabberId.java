package com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.Converter;
import com.thefirstlineofcode.basalt.oxm.conversion.converters.String2JabberIdConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Converter(String2JabberIdConverter.class)
public @interface String2JabberId {}
