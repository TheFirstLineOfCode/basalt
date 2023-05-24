package com.thefirstlineofcode.basalt.xeps.xdata.oxm;

import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.Validate;
import com.thefirstlineofcode.basalt.oxm.validation.ValidationException;
import com.thefirstlineofcode.basalt.xeps.xdata.Field;

public class XDataValidationClass {
	
	@Validate({"/field", "/reported/field", "/item/field"})
	public void validateField(Field field) throws ValidationException {
		if (field.getOptions() != null && field.getOptions().size() > 0) {
			if (field.getType() != Field.Type.LIST_MULTI && field.getType() != Field.Type.LIST_SINGLE)
				throw new ValidationException(String.format("options is only allowed in list_multi or list_single type field," +
						" but this is a %s type field", field.getType()));
		}
	}

}
