package com.thefirstlineofcode.basalt.oxm;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;


public class Value<T> {
	public enum Type {
		STRING,
		BYTE,
		BYTES,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		BIG_INT,
		DECIMAL,
		BOOLEAN
	}
	
	private T value;
	private Type type;
	
	private Value(T value, Type type) {
		this.value = value;
		this.type = type;
	}
	
	public T get() {
		return value;
	}
	
	public void set(T value) {
		this.value = value;
	}
	
	public Value.Type getType() {
		return type;
	}
	
	public static Value<?> createValue(Object object) {
		if (object == null) {
			return null;
		}
		
		Class<?> type = object.getClass();
		if (type == String.class) {
			return Value.createStringValue((String)object);
		} else if (type == Byte.class) {
			return Value.createByteValue((Byte)object);
		} else if (type == byte[].class) {
			return Value.createBytesValue((byte[])object);
		} if (type == Integer.class) {
			return Value.createIntValue((Integer)object);
		} else if (type == Long.class) {
			return Value.createLongValue((Long)object);
		} else if (type == BigInteger.class) {
			return Value.createBigIntValue((BigInteger)object);
		} else if (type == Float.class) {
			return Value.createFloatValue((Float)object);
		} else if (type == Double.class) {
			return Value.createDoubleValue((Double)object);
		} else if (type == BigDecimal.class) {
			return Value.createDecimalValue((BigDecimal)object);
		} else if (type == Boolean.class) {
			return Value.createBooleanValue((Boolean)object);
		} else {
			return Value.createStringValue(object.toString());
		}
	}
	
	public static Value<String> create(String value) {
		return createStringValue(value);
	}
	
	private static Value<String> createStringValue(String value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.STRING);
	}
	
	public static Value<Byte> create(Byte value) {
		return createByteValue(value);
	}
	
	private static Value<Byte> createByteValue(Byte value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.BYTE);
	}
	
	public static Value<byte[]> create(byte[] value) {
		return createBytesValue(value);
	}
	
	private static Value<byte[]> createBytesValue(byte[] value) {
		if (value == null || value.length == 0) {
			return null;
		}
		
		return new Value<byte[]>(value, Type.BYTES);
	}
	
	public static Value<Integer> create(Integer value) {
		return createIntValue(value);
	}
	
	private static Value<Integer> createIntValue(Integer value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.INT);
	}
	
	public static Value<Long> create(Long value) {
		return createLongValue(value);
	}
	
	private static Value<Long> createLongValue(Long value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.LONG);
	}
	
	public static Value<BigInteger> create(BigInteger value) {
		return createBigIntValue(value);
	}
	
	private static Value<BigInteger> createBigIntValue(BigInteger value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.BIG_INT);
	}
	
	public static Value<Float> create(Float value) {
		return createFloatValue(value);
	}
	
	private static Value<Float> createFloatValue(Float value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.FLOAT);
	}
	
	public static Value<Double> create(Double value) {
		return createDoubleValue(value);
	}
	
	private static Value<Double> createDoubleValue(Double value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.DOUBLE);
	}
	
	public static Value<BigDecimal> create(BigDecimal value) {
		return createDecimalValue(value);
	}
	
	private static Value<BigDecimal> createDecimalValue(BigDecimal value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.DECIMAL);
	}
	
	public static Value<Boolean> create(Boolean value) {
		return createBooleanValue(value);
	}
	
	private static Value<Boolean> createBooleanValue(Boolean value) {
		if (value == null) {
			return null;
		}
		
		return new Value<>(value, Type.BOOLEAN);
	}
	
	@SuppressWarnings("unchecked")
	public Value<String> stringIt() throws TypeConversionException {
		if (type == Type.STRING)
			return (Value<String>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<Byte> byteIt() throws TypeConversionException {
		if (type == Type.BYTE)
			return (Value<Byte>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<byte[]> bytesIt() throws TypeConversionException {
		if (type == Type.BYTES)
			return (Value<byte[]>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<Integer> intIt() throws TypeConversionException {
		if (type == Type.INT)
			return (Value<Integer>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<Long> longIt() throws TypeConversionException {
		if (type == Type.LONG)
			return (Value<Long>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<BigInteger> bigIntIt() throws TypeConversionException {
		if (type == Type.BIG_INT)
			return (Value<BigInteger>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<Float> floatIt() throws TypeConversionException {
		if (type == Type.FLOAT)
			return (Value<Float>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<Double> doubleIt() throws TypeConversionException {
		if (type == Type.DOUBLE)
			return (Value<Double>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<BigDecimal> decimalIt() throws TypeConversionException {
		if (type == Type.DECIMAL)
			return (Value<BigDecimal>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	@SuppressWarnings("unchecked")
	public Value<Boolean> booleanIt() throws TypeConversionException {
		if (type == Type.BOOLEAN)
			return (Value<Boolean>)this;
		
		throw new TypeConversionException(String.format("is %s", type));
	}
	
	public String getString() {
		try {
			return stringIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be string tye", e);
		}
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	public byte getByte() {
		try {
			return byteIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be byte type", e);
		}
	}
	
	public byte[] getBytes() {
		try {
			return bytesIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be bytes type", e);
		}
	}
	
	public int getInt() {
		try {
			return intIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be int type", e);
		}
	}
	
	public long getLong() {
		try {
			return longIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be long type", e);
		}
	}
	
	public BigInteger getBigInt() {
		try {
			return bigIntIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be BigInteger type", e);
		}
	}
	
	public float getFloat() {
		try {
			return floatIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be float type", e);
		}
	}
	
	public double getDouble() {
		try {
			return doubleIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be double type", e);
		}
	}
	
	public BigDecimal getDecimal() {
		try {
			return decimalIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be BigDecimal type", e);
		}
	}
	
	public boolean getBoolean() {
		try {
			return booleanIt().get();
		} catch (TypeConversionException e) {
			throw new BadMessageException("value should be boolean type", e);
		}
	}
}
