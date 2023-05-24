package com.thefirstlineofcode.basalt.oxm.coc.validation.factories;

import com.thefirstlineofcode.basalt.oxm.coc.validation.IValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.Email;
import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.validators.EmailValidator;

public class EmailValidatorFactory implements IValidatorFactory<Email> {
	private static final IValidator<String> validator = new EmailValidator();

	@Override
	public IValidator<String> create(Email annotation) {
		return validator;
	}

}
