package com.thefirstlineofcode.basalt.oxm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Attributes {
	private List<Attribute> attributes;
	
	public Attributes() {
		attributes = new ArrayList<>();
	}
	
	public Attributes(String localName, String value) {
		this(null, localName, value);
	}
	
	public Attributes(String prefix, String localName, String value) {
		this(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes(String localName, Value<?> value) {
		this(null, localName, value);
	}
	
	public Attributes(String prefix, String localName, Value<?> value) {
		this(new Attribute(prefix, localName, value));
	}
	
	public Attributes(Attribute attribute) {
		attributes = new ArrayList<>();
		attributes.add(attribute);
	}
	
	public Attributes add(Attribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Null attribute.");
		
		attributes.add(attribute);
		return this;
	}
	
	public Attributes add(String localName, Value<?> value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, Value<?> value) {
		if (value != null) {
			add(new Attribute(prefix, localName, value));
		}
		
		return this;
	}
	
	public Attributes add(String prefix, String localName, Integer value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, Integer value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, Long value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, Long value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, BigInteger value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, BigInteger value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, Float value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, Float value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, Double value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, Double value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, BigDecimal value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, BigDecimal value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, Boolean value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, boolean value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String prefix, String localName, String value) {
		return add(prefix, localName, value == null ? null : Value.create(value));
	}
	
	public Attributes add(String localName, String value) {
		return add(null, localName, value);
	}
	
	public Attributes add(String localName, Object object) {
		return add(null, localName, object);
	}
	
	public Attributes add(String prefix, String localName, Object object) {
		if (object != null) {
			return add(prefix, localName, object.toString());
		}
		
		return this;
	}
	
	public List<Attribute> get() {
		return attributes;
	}
	
	public int size() {
		return attributes.size();
	}
}
