package com.thefirstlineofcode.basalt.oxm.xep.ibr;

import java.util.ArrayList;
import java.util.List;

public class TRegistrationForm {
	private boolean registered = false;
	private List<TRegistrationField> fields;
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public List<TRegistrationField> getFields() {
		if (fields == null)
			fields = new ArrayList<>();
		
		return fields;
	}

	public void setFields(List<TRegistrationField> fields) {
		this.fields = fields;
	}
	
}
