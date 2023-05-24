package com.thefirstlineofcode.basalt.oxm.conversion.validators;

import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.ValidationException;

public class TUrlValidator implements IValidator<String> {

	@Override
	public void validate(String url) throws ValidationException {
		org.apache.commons.validator.UrlValidator validator = new org.apache.commons.validator.UrlValidator();
		if (!validator.isValid(url)) {
			throw new ValidationException(String.format("%s isn't a valid url", url));
		}
	}

}
