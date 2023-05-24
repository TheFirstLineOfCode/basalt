package com.thefirstlineofcode.basalt.oxm.validation.validators;

import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.ValidationException;

public class NotNullValidator<T> implements IValidator<T>  {

	@Override
	public void validate(Object object) throws ValidationException {
		if (object == null || "".equals(object)) {
			throw new ValidationException("Value mustn't be null.");
		}
	}

}
