package com.thefirstlineofcode.basalt.oxm.coc.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thefirstlineofcode.basalt.oxm.coc.validation.Validator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validator
public @interface NotNull {
}
