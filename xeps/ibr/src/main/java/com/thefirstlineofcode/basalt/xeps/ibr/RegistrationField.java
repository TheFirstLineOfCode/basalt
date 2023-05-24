package com.thefirstlineofcode.basalt.xeps.ibr;

public class RegistrationField {
	private String name;
	private String value;
	
	public RegistrationField(String name) {
		this(name, null);
	}
	
	public RegistrationField(String name, String value) {
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
