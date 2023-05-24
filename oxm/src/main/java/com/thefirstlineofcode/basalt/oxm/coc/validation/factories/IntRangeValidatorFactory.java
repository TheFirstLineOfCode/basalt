package com.thefirstlineofcode.basalt.oxm.coc.validation.factories;

import com.thefirstlineofcode.basalt.oxm.coc.validation.IValidatorFactory;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.IntRange;
import com.thefirstlineofcode.basalt.oxm.validation.IValidator;
import com.thefirstlineofcode.basalt.oxm.validation.validators.IntRangeValidator;

public class IntRangeValidatorFactory implements IValidatorFactory<IntRange> {

	@Override
	public IValidator<Integer> create(IntRange intRange) {
		return new IntRangeValidator(intRange.min(), intRange.max());
	}

}
