package com.thefirstlineofcode.basalt.oxm.coc;

import static java.util.Locale.ENGLISH;

import java.lang.reflect.Method;

public class PropertyDescriptor {
	private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";
    
	private Class<?> beanClass;
	private String propertyName;
	private volatile Method readMethod;
	private volatile Method writeMethod;
	private Class<?> propertyType;
	
	public PropertyDescriptor(String propertyName, Class<?> beanClass) throws IntrospectionException {
		if (beanClass == null) {
			throw new IntrospectionException("Target bean class is null.");
		}
		
		if (propertyName == null || propertyName.length() == 0) {
			throw new IntrospectionException("Bad property name.");
		}
		
		this.beanClass = beanClass;
		this.propertyName = propertyName;
		
		readMethod = getReadMethod();
		if (readMethod == null) {
			throw new IntrospectionException("Read method not found.");
		}
		
		writeMethod = getWriteMethod();
		if (writeMethod == null) {
			throw new IntrospectionException("Write method not found.");
		}
		
		propertyType = getPropertyType();
	}

	public Method getWriteMethod() {
		if (writeMethod != null)
			return writeMethod;
		
		synchronized (this) {
			if (writeMethod != null) {
				return writeMethod;
			}
			
			writeMethod = doGetWriteMethod();
		}
		
		return writeMethod;
	}

	private Method doGetWriteMethod() {
		Class<?> propertyType = readMethod.getReturnType();
		if (propertyType == Void.TYPE) {
			return null;
		}
		
		String writeMethodName = SET_PREFIX + capitalize(propertyName);
		try {
			return beanClass.getMethod(writeMethodName, new Class<?>[] {propertyType});
		} catch (Exception e) {
			return null;
		}
	}

	public Method getReadMethod() {
		if (readMethod != null)
			return readMethod;
		
		synchronized (this) {
			if (readMethod != null) {
				return readMethod;
			}
			
			try {
				readMethod = doGetReadMethod();
			} catch (Exception e) {
				return null;
			}
			
			if (readMethod.getParameterTypes().length != 0) {
				return null;
			}
		}
		
		return readMethod;
	}

	private Method doGetReadMethod() throws NoSuchMethodException, SecurityException {
		String readMethodName = IS_PREFIX + capitalize(propertyName);
		
		Method method = null;
		try {
			method = beanClass.getMethod(readMethodName, new Class<?>[0]);
			Class<?> booleanType = method.getReturnType();
			if (booleanType != Boolean.TYPE && booleanType != Boolean.class) {
				return null;
			}
		} catch (Exception e) {
			// ignore
		}
		
		if (method == null) {
			readMethodName = GET_PREFIX + capitalize(propertyName);
			method = beanClass.getMethod(readMethodName, new Class<?>[0]);
		}
		
		return method;
	}
	
	private String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }
	
	public String getName() {
		return propertyName;
	}
	
	public synchronized Class<?> getPropertyType() {
		if (propertyType != null)
			return propertyType;
		
		propertyType = readMethod.getReturnType();
		
		return propertyType;
	}
}
