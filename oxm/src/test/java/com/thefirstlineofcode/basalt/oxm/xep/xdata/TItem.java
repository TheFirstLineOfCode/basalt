package com.thefirstlineofcode.basalt.oxm.xep.xdata;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;

public class TItem {
	@Array(value=TField.class, elementName="field")
	private List<TField> fields;

	public List<TField> getFields() {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		
		return fields;
	}

	public void setFields(List<TField> fields) {
		this.fields = fields;
	}
	
}
