package com.thefirstlineofcode.basalt.oxm.coc.validation;

import java.lang.annotation.Annotation;

import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.CustomValidator;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.Email;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.IntRange;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.NotNull;
import com.thefirstlineofcode.basalt.oxm.coc.validation.factories.CustomValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.coc.validation.factories.EmailValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.coc.validation.factories.IntRangeValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.coc.validation.factories.NotNullValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.ValidationException;

public class ValidationService {
	private static IValidationFactory validationFactory = new ValidationFactory();
	
	static {
		validationFactory.register(CustomValidator.class, new CustomValidatorFactory());
		validationFactory.register(NotNull.class, new NotNullValidatorFactory());
		validationFactory.register(IntRange.class, new IntRangeValidatorFactory());
		validationFactory.register(Email.class, new EmailValidatorFactory());
	}
	
	public static <T extends Annotation> void register(Class<T> type, IValidatorFactory<T> factory) {
		validationFactory.register(type, factory);
	}
	
	public static void unregister(Class<? extends Annotation> type) {
		validationFactory.unregister(type);
	}
	
	public static <T> void validate(Annotation annotation, T value) throws ValidationException {
		@SuppressWarnings("unchecked")
		IValidator<T> validator = (IValidator<T>)validationFactory.getValidator(annotation);
		
		if (validator == null) {
			throw new ValidationException(String.format("Validator factory of annotation class %s not be found.",
					annotation.annotationType().getName()));
		}
		
		validator.validate(value);
	}
}
