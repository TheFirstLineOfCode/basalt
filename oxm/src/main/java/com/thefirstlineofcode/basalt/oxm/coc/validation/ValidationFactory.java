package com.thefirstlineofcode.basalt.oxm.coc.validation;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thefirstlineofcode.basalt.oxm.validation.IValidator;

public class ValidationFactory implements IValidationFactory {
	private Map<Class<?>, IValidatorFactory<?>> validatorFactories;
	
	public ValidationFactory() {
		this.validatorFactories = new ConcurrentHashMap<>();
	}

	@Override
	public <T extends Annotation> void register(Class<T> type, IValidatorFactory<T> validatorFactory) {
		validatorFactories.put(type, validatorFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> IValidatorFactory<T> unregister(Class<T> type) {
		return (IValidatorFactory<T>)validatorFactories.remove(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> IValidator<?> getValidator(T annotation) {
		IValidatorFactory<T> factory = (IValidatorFactory<T>)validatorFactories.get(annotation.annotationType());
		if (factory == null)
			return null;
		
		return factory.create(annotation);
	}

}
