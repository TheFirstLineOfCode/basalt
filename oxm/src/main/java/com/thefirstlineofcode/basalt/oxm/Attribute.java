package com.thefirstlineofcode.basalt.oxm;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Attribute {
	private String prefix;
	private String localName;
	private Value<?> value;
	
	public Attribute(String localName, Value<?> value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, Value<?> value) {
		if (localName == null)
			throw new IllegalArgumentException("Null local name.");
		
		this.prefix = prefix;
		this.localName = localName;
		this.value = value;
	}
	
	public Attribute(String localName, String value) {
		this(null, localName, Value.create(value));
	}
	
	public Attribute(String prefix, String localName, String value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, boolean value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, boolean value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, int value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, int value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, long value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, long value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, BigInteger value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, BigInteger value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, float value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, float value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, double value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, double value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, BigDecimal value) {
		this(null, localName, value);
	}
	
	public Attribute(String prefix, String localName, BigDecimal value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, Byte value) {
		this(null, localName, Value.create(value));
	}
	
	public Attribute(String prefix, String localName, Byte value) {
		this(prefix, localName, Value.create(value));
	}
	
	public Attribute(String localName, byte[] value) {
		this(null, localName, Value.create(value));
	}
	
	public Attribute(String prefix, String localName, byte[] value) {
		this(prefix, localName, Value.create(value));
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getLocalName() {
		return localName;
	}
	
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	
	public Value<?> getValue() {
		return value;
	}
	
	public void setValue(Value<?> value) {
		this.value = value;
	}
	
	public String getName() {
		if (prefix == null) {
			return localName;
		} else {
			return String.format("%s:%s", prefix, localName);
		}
	}
}
