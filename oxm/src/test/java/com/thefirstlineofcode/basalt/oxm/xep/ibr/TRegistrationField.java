package com.thefirstlineofcode.basalt.oxm.xep.ibr;

public class TRegistrationField {
	private String name;
	private String value;
	
	public TRegistrationField(String name) {
		this(name, null);
	}
	
	public TRegistrationField(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
