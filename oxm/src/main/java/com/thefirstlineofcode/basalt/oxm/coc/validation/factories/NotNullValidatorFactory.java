package com.thefirstlineofcode.basalt.oxm.coc.validation.factories;

import com.thefirstlineofcode.basalt.oxm.coc.validation.IValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.NotNull;
import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.validators.NotNullValidator;

public class NotNullValidatorFactory implements IValidatorFactory<NotNull> {

	@Override
	public IValidator<Object> create(NotNull notNull) {
		return new NotNullValidator<>();
	}

}
