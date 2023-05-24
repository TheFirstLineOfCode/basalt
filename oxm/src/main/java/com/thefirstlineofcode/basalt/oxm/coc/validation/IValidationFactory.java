package com.thefirstlineofcode.basalt.oxm.coc.validation;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.validation.IValidator;

public interface IValidationFactory {
	<T extends Annotation> void register(Class<T> type, IValidatorFactory<T> validatorFactory);
	<T extends Annotation> IValidatorFactory<T> unregister(Class<T> type);
	
	<T extends Annotation> IValidator<?> getValidator(T annotation);
}
