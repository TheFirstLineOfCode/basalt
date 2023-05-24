package com.thefirstlineofcode.basalt.oxm.validation.validators;

import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.ValidationException;

public class IntRangeValidator implements IValidator<Integer> {
	private int min;
	private int max;
	
	public IntRangeValidator(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public void validate(Integer obj) throws ValidationException {
		if (obj < min || obj > max) {
			throw new ValidationException(String.format("Value should be between %d and %d, but is %d.",
					min, max, obj));
		}
	}

}
