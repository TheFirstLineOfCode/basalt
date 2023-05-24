package com.thefirstlineofcode.basalt.xeps.xdata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.Converter;
import com.thefirstlineofcode.basalt.xeps.xdata.oxm.String2XDataTypeConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Converter(String2XDataTypeConverter.class)
public @interface String2XDataType {
}
