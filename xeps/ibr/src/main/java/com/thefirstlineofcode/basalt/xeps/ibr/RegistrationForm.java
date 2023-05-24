package com.thefirstlineofcode.basalt.xeps.ibr;

import java.util.ArrayList;
import java.util.List;

public class RegistrationForm {
	private boolean registered = false;
	private List<RegistrationField> fields;
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public List<RegistrationField> getFields() {
		if (fields == null)
			fields = new ArrayList<>();
		
		return fields;
	}

	public void setFields(List<RegistrationField> fields) {
		this.fields = fields;
	}
	
}
