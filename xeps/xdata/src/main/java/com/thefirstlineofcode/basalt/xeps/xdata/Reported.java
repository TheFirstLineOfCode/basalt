package com.thefirstlineofcode.basalt.xeps.xdata;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;

public class Reported {
	@Array(Field.class)
	private List<Field> fields;

	public List<Field> getFields() {
		if (fields == null)
			fields = new ArrayList<>();
		
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
}
