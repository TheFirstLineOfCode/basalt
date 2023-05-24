package com.thefirstlineofcode.basalt.oxm.coc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.binary.BinaryUtils;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.BindTo;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.BooleanOnly;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.NotBind;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.Text;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.ConversionService;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.Converter;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.ConverterFactory;
import com.thefirstlineofcode.basalt.oxm.coc.validation.ValidationService;
import com.thefirstlineofcode.basalt.oxm.coc.validation.Validator;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.Validate;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.ValidationClass;
import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.oxm.parsing.ParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingPath;
import com.thefirstlineofcode.basalt.oxm.validation.ValidationException;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class CocParser<T> extends ParserAdaptor<T> {
	private static final String PROTOCOL_OBJECT_FLAG = "$$p$$";
	
	private static final Object KEY_PARSING_OBJECTS_STACK = new Object();
	
	private Map<String, IElementAccessor> elementAccessors;
	private Map<String, PropertyDescriptor> embeddedObjectAccessors;
	
	private volatile boolean scaned;
	
	private Object validationClass;
	private Map<String, Method> validationMethods;
	
	private static Method arrayElementWritter;
	
	static {
		try {
			arrayElementWritter = List.class.getMethod("add", new Class<?>[] {Object.class});
		} catch (Exception e) {
			throw new RuntimeException("Can't get list.add() method!!!");
		}
	}
	
	public CocParser(Class<T> objectType) {
		super(objectType);
		embeddedObjectAccessors = new HashMap<>();
		validationClass = null;
		validationMethods = new HashMap<>();
		scaned = false;
	}
	
	@Override
	public IElementParser<T> getElementParser(IParsingPath parsingPath) {
		if (scaned)
			return doGetElementParser(parsingPath.toString());
		
		synchronized (this) {
			if (scaned)
				return doGetElementParser(parsingPath.toString());
			
			elementAccessors = new HashMap<>();
			try {
				scanType(new ParsingPath(), new DefaultElementAccessor(objectType, null));
			} catch (IntrospectionException e) {
				throw new RuntimeException("Failed to get element parser.", e);
			}
			
			try {
				scanValidationClass();
			} catch (Exception e) {
				throw new RuntimeException("Failed to scan validation class.", e);
			}
			
			scaned = true;
		}
		
		return doGetElementParser(parsingPath.toString());
	}
	
	private void scanValidationClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
				InvocationTargetException, NoSuchMethodException, SecurityException {
		ValidationClass vcAnnotation = objectType.getAnnotation(ValidationClass.class);
		if (vcAnnotation == null)
			return;
		
		Class<?> validationClassType = vcAnnotation.value();
		if (validationClassType == Object.class) {
			validationClassType = objectType;
		}
		
		validationClass = validationClassType.getDeclaredConstructor().newInstance();
		
		for (Method method : validationClassType.getMethods()) {
			Validate validate = method.getAnnotation(Validate.class);
			if (validate == null)
				continue;
			
			String[] paths = validate.value();
			
			for (String path : paths) {
				validationMethods.put(path, method);
			}
		}
	}

	private IElementParser<T> doGetElementParser(String parsingPathString) {
		IElementAccessor elementAccessor = elementAccessors.get(parsingPathString);
		if (elementAccessor == null) {
			return null;
		}
		
		return new DefaultElementParser<>(elementAccessor);
	}
	
	private void scanType(ParsingPath parsingPath, IElementAccessor elementAccessor) throws IntrospectionException {
		Class<?> elementType = elementAccessor.getType();
		
		if (elementType == null)
			return;
		
		nextField:
		for (Field field : elementType.getDeclaredFields()) {
			PropertyDescriptor propertyDescriptor = null;
			try {
				propertyDescriptor = new PropertyDescriptor(field.getName(), elementType);
			} catch (IntrospectionException e) {
				continue;
			}
			
			if (propertyDescriptor.getReadMethod() == null || propertyDescriptor.getWriteMethod() == null)
				continue;
			
			Text text = null;
			BindTo bindTo = null;
			Array array = null;
			TextOnly textOnly = null;
			BooleanOnly booleanOnly = null;
			Annotation converterAnnotation = null;
			List<Annotation> validatorAnnotations = new ArrayList<>();
			
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				Class<?> annotationType = annotation.annotationType();
				if (annotationType == NotBind.class)
					continue nextField;
				
				if (annotationType == Text.class) {
					text = (Text)annotation;
				} else if (annotationType == BindTo.class) {
					bindTo = (BindTo)annotation;
				} else if (annotationType == Array.class) {
					array = (Array)annotation;
				} else if (annotationType == TextOnly.class) {
					textOnly = (TextOnly)annotation;
				} else if (annotationType == BooleanOnly.class) {
					booleanOnly = (BooleanOnly)annotation;
				} else if (annotation.annotationType().getAnnotation(Converter.class) != null ||
						annotation.annotationType().getAnnotation(ConverterFactory.class) != null) {
					converterAnnotation = annotation;
				} else if (annotation.annotationType().getAnnotation(Validator.class) != null) {
					validatorAnnotations.add(annotation);
				}
			}
			
			ProtocolObject embeddedObject = getEmbeddedObject(field);
			
			String name = getBindingName(field, embeddedObject, bindTo, text);
			
			Class<?> fieldType = field.getType();
			if (isTextField(text, field, elementType)) {
				String textObjectName = field.getName();
				elementAccessor.putPropertyAccessor(textObjectName, new DefaultPropertyAccessor(propertyDescriptor,
						converterAnnotation, validatorAnnotations));
				elementAccessor.setTextObjectName(textObjectName);
			} else if (isArray(array, fieldType)) {
				elementAccessors.put(parsingPath.toString(), new NopElementAccessor());
				scanArrayElementType(parsingPath, array, textOnly, converterAnnotation,
						validatorAnnotations, propertyDescriptor);
			} else if (isTextOnly(textOnly) && isTextOnlyType(fieldType, converterAnnotation)) {
				parsingPath.enter(name);
				elementAccessors.put(parsingPath.toString(), new TextOnlyElementAccessor(fieldType, propertyDescriptor,
						name, field.getName(), converterAnnotation, validatorAnnotations));
				parsingPath.exit();
			} else if (isBooleanOnly(booleanOnly, field, elementType)) {
				parsingPath.enter(name);
				elementAccessors.put(parsingPath.toString(), new BooleanOnlyElementAccessor(propertyDescriptor));
				parsingPath.exit();
			} else if (embeddedObject != null) {
				if (!name.startsWith(PROTOCOL_OBJECT_FLAG)) {
					name = PROTOCOL_OBJECT_FLAG + name;
				}
				
				embeddedObjectAccessors.put(name, propertyDescriptor);
			} else if (isPrimitiveType(fieldType)) {
				elementAccessor.putPropertyAccessor(name, new DefaultPropertyAccessor(propertyDescriptor,
						converterAnnotation, validatorAnnotations));
			} else if (isBytes(fieldType)) {
				elementAccessor.putPropertyAccessor(name, new DefaultPropertyAccessor(propertyDescriptor,
						converterAnnotation, validatorAnnotations));				
			} else if (isByte(fieldType)) {
				elementAccessor.putPropertyAccessor(name, new DefaultPropertyAccessor(propertyDescriptor,
						converterAnnotation, validatorAnnotations));				
			} else if (isConvertible(converterAnnotation)) {
				elementAccessor.putPropertyAccessor(name, new DefaultPropertyAccessor(propertyDescriptor,
						converterAnnotation, validatorAnnotations));
			} else {
				parsingPath.enter(name);
				scanType(parsingPath, new DefaultElementAccessor(getFieldType(field), propertyDescriptor));
				parsingPath.exit();
			}
		}
		
		elementAccessors.put(parsingPath.toString(), elementAccessor);
	}

	private boolean isBytes(Class<?> fieldType) {
		return fieldType == byte[].class;
	}
	
	private boolean isByte(Class<?> fieldType) {
		return fieldType == byte.class ||
				fieldType == Byte.class;
	}

	private Class<?> getFieldType(Field field) {
		if (field.getType() == long.class) {
			return Long.class;
		} else if (field.getType() == int.class) {
			return Integer.class;
		} else if (field.getType() == float.class) {
			return Float.class;
		} else if (field.getType() == double.class) {
			return Double.class;
		} else if (field.getType() == boolean.class) {
			return Boolean.class;
		} else if (field.getType() == byte.class) {
			return Byte.class;
		} else {
			return field.getType();
		}
		
	}

	private ProtocolObject getEmbeddedObject(Field field) {
		ProtocolObject embeddedObject = field.getType().getAnnotation(ProtocolObject.class);
		
		if (embeddedObject != null) {
			return embeddedObject;
		}
		
		return null;
	}

	private boolean isTextOnlyType(Class<?> fieldType, Annotation converterAnnotation) {
		if (converterAnnotation != null)
			return true;
		
		return isStringType(fieldType) || isPrimitiveType(fieldType);
	}

	private class BooleanOnlyElementAccessor extends DefaultElementAccessor {
		public BooleanOnlyElementAccessor(PropertyDescriptor ownAccessor) {
			super(null, ownAccessor);
		}

		@Override
		public void putPropertyAccessor(String name, IPropertyAccessor propertyAccessor) {}

		@Override
		public IPropertyAccessor getPropertyAccessor(String name) {
			return null;
		}

		@Override
		public void setTextObjectName(String textObjectName) {}

		@Override
		public String getTextObjectName() {
			return null;
		}

		@Override
		public Class<?> getType() {
			return null;
		}

		@Override
		public void write(IParsingContext<?> context, Object parent, Object element) {
			try {
				ownAccessor.getWriteMethod().invoke(parent, true);
			} catch (Exception e) {
				throw new RuntimeException(String.format("%s can't set element value", ownAccessor.getName()), e);
			}
		}

		@Override
		public Object create(IParsingContext<?> context) {
			return null;
		}

		@Override
		public Object remove(IParsingContext<?> context) {
			return null;
		}

	}

	private boolean isBooleanOnly(BooleanOnly bool, Field field, Class<?> elementType) {
		if (bool != null) {
			if (!field.getType().equals(boolean.class)) {
				throw new BadMessageException(String.format("BoolOnly field %s.%s should be boolean or Boolean type.",
						elementType.getName(), field.getName()));
			}
			
			return true;
		} else {
			return false;
		}
	}

	private String getBindingName(Field field, ProtocolObject embeddedObject, BindTo bindTo, Text text) {
		String name = null;
		
		if (embeddedObject != null) {
			name = embeddedObject.localName();
		}
		
		if (bindTo != null) {
			name = bindTo.value();
		}
		
		if (name == null) {
			name = getNameByNamingConvention(field.getName());
		}
		
		return name;
	}

	private boolean isConvertible(Annotation converterAnnotation) {
		return converterAnnotation != null;
	}

	private boolean isStringType(Class<?> fieldType) {
		return String.class.equals(fieldType);
	}

	private boolean isTextOnly(TextOnly textOnly) {
		return textOnly != null;
	}
	
	private void scanArrayElementType(ParsingPath parsingPath, Array array, TextOnly textOnly,
			Annotation converterAnnotation, List<Annotation> validatorAnnotations, PropertyDescriptor propertyDescriptor)
					throws IntrospectionException {
		Class<?> elementType = array.value();
		
		String arrayElementName = getArrayElementName(array.elementName(), elementType);
		if (arrayElementName == null) {
			throw new RuntimeException(String.format("Null array element name. current parsing path: %s.", parsingPath));
		}

		parsingPath.enter(arrayElementName);
		
		if (isTextOnly(textOnly)) {
			elementAccessors.put(parsingPath.toString(), new TextOnlyArrayItemElementAccessor(elementType, converterAnnotation, validatorAnnotations, propertyDescriptor));
		} else {
			scanType(parsingPath, new ArrayItemElementAccessor(elementType, propertyDescriptor));
		}
		parsingPath.exit();
	}
	
	private String getArrayElementName(String elementName, Class<?> elementType) {
		if (elementName != null && !elementName.equals(""))
			return elementName;
		
		if (isPrimitiveType(elementType)) {
			throw new IllegalArgumentException("Element type is primitive type. Element name shouldn't be null.");
		}
		
		return getNameByNamingConvention(elementType.getSimpleName());
	}

	private boolean isArray(Array array, Class<?> fieldType) {
		return List.class.isAssignableFrom(fieldType) && array != null;
	}

	private boolean isTextField(Text text, Field field, Class<?> elementType) {
		if (text != null) {
			if (!field.getType().equals(String.class)) {
				throw new BadMessageException(String.format("Text field %s.%s should be string type.",
						elementType.getName(), field.getName()));
			}
			
			return true;
		} else {
			return false;
		}
	}
		
	private class DefaultElementParser<V> implements IElementParser<V> {
		private IElementAccessor elementAccessor;
		
		public DefaultElementParser(IElementAccessor elementAccessor) {
			this.elementAccessor = elementAccessor;
		}

		@Override
		public void processAttributes(IParsingContext<V> context, List<Attribute> attributes) {
			Object parsingObject = createParsingObject(context, elementAccessor);
			
			for (Attribute attribute : attributes) {
				IPropertyAccessor pa = elementAccessor.getPropertyAccessor(attribute.getName());
				if (pa == null) {
					throw new BadMessageException(String.format("Attribute '%s' not allowed in here: %s.",
							attribute.getName(), context));
				}
				
				try {
					pa.write(context, parsingObject, attribute.getValue().get());
				} catch (ConversionException e) {
					throw new BadMessageException(String.format("Conversion error: attribute '%s'. Parsing context: %s.",
							attribute.getName(), context), e);
				} catch (ValidationException e) {
					throw new BadMessageException(String.format("Validation error: attribute '%s'. Parsing context: %s.",
							attribute.getName(), context), e);
				}
			}
		}
		
		@Override
		public void processText(IParsingContext<V> context, Value<?> text) {
			Object parsingObject = getParsingObject(context, elementAccessor);
			
			if (text != null) {
				String textObjectName = elementAccessor.getTextObjectName();
				IPropertyAccessor pa = elementAccessor.getPropertyAccessor(textObjectName);
				if (pa == null) {
					throw new BadMessageException(String.format("Can't get property accessor for text. Parsing context: %s.",
							context));
				}
				
				try {
					pa.write(context, parsingObject, text.get());
				} catch (ConversionException e) {
					throw new BadMessageException(String.format("Conversion error: text '%s'. Parsing context: %s.",
							text, context), e);
				} catch (ValidationException e) {
					throw new BadMessageException(String.format("Validation error: text '%s'. Parsing context: %s.",
							text, context), e);
				}
			}
				
			Object removed = removeParsingObject(context, elementAccessor);
			if (removed == context.getObject()) { // root object removed
				context.removeAttribute(KEY_PARSING_OBJECTS_STACK);
				validateElement(context, removed);
			} else {
				Object parentObject = getParsingObject(context, elementAccessor);
				if (parentObject != null) {
					elementAccessor.write(context, parentObject, parsingObject);
				}
			}
		}
		
		protected Object removeParsingObject(IParsingContext<V> context, IElementAccessor elementAccessor) {
			return elementAccessor.remove(context);
		}
		
		protected Object createParsingObject(IParsingContext<V> context, IElementAccessor elementAccessor) {
			return elementAccessor.create(context);
		}
		
		protected Object getParsingObject(IParsingContext<V> context, IElementAccessor elementAccessor) {
			return elementAccessor.get(context);
		}
		
	}
	
	private String getNameByNamingConvention(String name) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('-').append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		
		if (sb.length() > 1 && sb.charAt(0) == '-') {
			sb.delete(0, 1);
		}
		
		return sb.toString();
	}

	private boolean isPrimitiveType(Class<?> fieldType) {
		return fieldType.equals(String.class) ||
				fieldType.equals(boolean.class) ||
				fieldType.equals(Boolean.class) ||
				fieldType.equals(int.class) ||
				fieldType.equals(Integer.class) ||
				fieldType.equals(long.class) ||
				fieldType.equals(Long.class) ||
				fieldType.equals(float.class) ||
				fieldType.equals(Float.class) ||
				fieldType.equals(double.class) ||
				fieldType.equals(Double.class) ||
				fieldType.equals(BigInteger.class) ||
				fieldType.equals(BigDecimal.class) ||
				fieldType.isEnum();
	}

	@Override
	public void processEmbeddedObject(IParsingContext<T> context, Protocol protocol, Object embedded) {
		PropertyDescriptor pd = embeddedObjectAccessors.get(PROTOCOL_OBJECT_FLAG + protocol.getLocalName());
		if (pd == null) {
			throw new BadMessageException(String.format("Embedded object type %s not allowed in class %s.",
					embedded.getClass(), context.getObject()));
		}
		
		Method writter = pd.getWriteMethod();
		try {
			writter.invoke(context.getObject(), new Object[]{embedded});
		} catch (Exception e) {
			throw new RuntimeException(String.format("Failed to invoke write method '%s'. current object: %s.",
					pd.getName(), context.getObject().getClass().getName()), e);
		}
	}
	
	private static interface IElementAccessor {
		void putPropertyAccessor(String name, IPropertyAccessor propertyAccessor);
		IPropertyAccessor getPropertyAccessor(String name);
		void setTextObjectName(String textObjectName);
		String getTextObjectName();
		public Class<?> getType();
		public Object create(IParsingContext<?> context);
		public Object get(IParsingContext<?> context);
		public Object remove(IParsingContext<?> context);
		public void write(IParsingContext<?> context, Object parent, Object element);
	}
	
	private class DefaultElementAccessor implements IElementAccessor {
		protected String textObjectName;
		protected Map<String, IPropertyAccessor> propertyAccessors;
		protected PropertyDescriptor ownAccessor;
		protected Class<?> type;
		
		public DefaultElementAccessor(Class<?> type, PropertyDescriptor ownAccessor) {
			this.type = type;
			this.ownAccessor = ownAccessor;
			propertyAccessors = new HashMap<>();
		}
		
		@Override
		public void putPropertyAccessor(String name, IPropertyAccessor propertyAccessor) {
			propertyAccessors.put(name, propertyAccessor);
		}
		
		@Override
		public IPropertyAccessor getPropertyAccessor(String name) {
			return propertyAccessors.get(name);
		}
		
		@Override
		public void setTextObjectName(String textObjectName) {
			this.textObjectName = textObjectName;
		}
		
		@Override
		public String getTextObjectName() {
			return textObjectName;
		}
		
		@Override
		public Class<?> getType() {
			return type;
		}
		
		@Override
		public void write(IParsingContext<?> context, Object parent, Object element) {
			validateElement(context, element);
			
			try {
				ownAccessor.getWriteMethod().invoke(parent, new Object[] {element});
			} catch (Exception e) {
				throw new RuntimeException(String.format("%s can't set element value.", ownAccessor.getName()), e);
			}
		}

		@Override
		public Object create(IParsingContext<?> context) {
			Stack<Object> stack = context.getAttribute(KEY_PARSING_OBJECTS_STACK);
			if (stack == null) {
				stack = new Stack<>();
				context.setAttribute(KEY_PARSING_OBJECTS_STACK, stack);
				stack.push(context.getObject());
			} else {
				Object object = create();
				if (object != null) {
					stack.push(object);
				}
			}
			
			return stack.peek();
		}
		
		protected Object create() {
			if (type == null)
				return null;
			
			try {
				if (isPrimitiveType(type)) {
					return createPrimitiveTypeObject();
				} else {
					return type.getDeclaredConstructor().newInstance();					
				}
			} catch (Exception e) {
				throw new RuntimeException(String.format("Can't create element instance. class name %s.", type.getName()));
			}
		}

		private Object createPrimitiveTypeObject() {
			if (type == long.class) {
				return Long.valueOf(0);
			}
			
			if (type == int.class) {
				return Integer.valueOf(0);
			}
			
			if (type == float.class) {
				return Float.valueOf(0);
			}
			
			if (type == double.class) {
				return Double.valueOf(0);
			}
			
			if (type == boolean.class) {
				return Boolean.valueOf(false);
			}
			
			throw new IllegalArgumentException(String.format("Unknown primitive type %s", type.getName()));
		}

		@Override
		public Object get(IParsingContext<?> context) {
			Stack<Object> stack = context.getAttribute(KEY_PARSING_OBJECTS_STACK);
			if (stack == null)
				return null;
			
			return stack.peek();
		}

		@Override
		public Object remove(IParsingContext<?> context) {
			Stack<Object> stack = context.getAttribute(KEY_PARSING_OBJECTS_STACK);
			if (stack == null) {
				return null;
			}
			
			if (getType() == null) {
				// it must be a text only or boolean only element. the two kinds of element don't create a parsing object.
				return null;
			}
			
			return stack.pop();
		}
	}
	
	private void validateElement(IParsingContext<?> context, Object element) {
		if (validationClass == null)
			return;
		
		Method method = validationMethods.get(context.getParsingPath().toString());
		
		if (method != null) {
			try {
				method.invoke(validationClass, new Object[] {element});
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(String.format("Validation method error. Element: %s. Context: %s.",
						element, context));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(String.format("Validation method error. Element: %s. Context: %s.",
						element, context));
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof ValidationException) {
					throw new BadMessageException(String.format("Validation error: element '%s'. Parsing context: %s.",
							element, context), e.getTargetException());
				} else {
					throw new RuntimeException(String.format("Validation method error. Element: %s. Context: %s.",
							element, context));
				}
			}
		}
	}
	
	private class ArrayItemElementAccessor extends DefaultElementAccessor {
		public ArrayItemElementAccessor(Class<?> type, PropertyDescriptor ownAccessor) {
			super(type, ownAccessor);
		}
		
		public void write(IParsingContext<?> context, Object parent, Object element) {
			validateElement(context, element);
			
			try {
				Object array = ownAccessor.getReadMethod().invoke(parent, new Object[] {});
				arrayElementWritter.invoke(array, new Object[] {element});
			} catch (Exception e) {
				throw new RuntimeException(String.format("%s can't add element value to array.", arrayElementWritter.getName()), e);
			}
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public Object create(IParsingContext<?> context) {
			Object parent = get(context);
			if (parent == null) {
				parent = super.create(context);
			}
			
			try {
				Object array = ownAccessor.getReadMethod().invoke(parent, new Object[] {});
				if (array == null) {
					array = new ArrayList();
					ownAccessor.getWriteMethod().invoke(parent, new Object[] {array});
				}
			} catch (Exception e) {
				throw new RuntimeException(String.format("%s can't set element value.", ownAccessor.getName()), e);
			}
			
			return super.create(context);
		}
		
	}
	
	private class TextOnlyElementAccessor extends DefaultElementAccessor {
		private String name;
		private String textObjectName;
		private PropertyDescriptor pd;
		private Annotation converterAnnotation;
		private List<Annotation> validatorAnnotations;
		private Class<?> fieldType;
		
		public TextOnlyElementAccessor(Class<?> fieldType, PropertyDescriptor ownAccessor, String name, String textObjectName,
				Annotation converterAnnotation, List<Annotation> validatorAnnotations) {
			super(null, ownAccessor);
			this.fieldType = fieldType;
			this.name = name;
			this.textObjectName = textObjectName;
			this.converterAnnotation = converterAnnotation;
			this.validatorAnnotations = validatorAnnotations;
		}
		
		@Override
		public String getTextObjectName() {
			return textObjectName;
		}
		
		@Override
		public IPropertyAccessor getPropertyAccessor(String name) {
			if (this.textObjectName.equals(name)) {
				return new IPropertyAccessor() {
					
					@Override
					public void write(IParsingContext<?> context, Object object, Object value) {
						Stack<Object> stack = context.getAttribute(KEY_PARSING_OBJECTS_STACK);
						if (stack == null) {
							throw new RuntimeException("Null parsing objects stack!!!???");
						}
						
						Object parent = stack.peek();
						if (pd == null) {
							try {
								pd = new PropertyDescriptor(TextOnlyElementAccessor.this.textObjectName, parent.getClass());
							} catch (IntrospectionException e) {
								throw new RuntimeException(String.format("Can't get property descriptor of %s.%s.",
										parent.getClass().getName(), TextOnlyElementAccessor.this.name), e);
							}
						}
						
						try {
							value = convert(converterAnnotation, value);
						} catch (ConversionException e) {
							// impossible
						}
						
						if ((value instanceof String) && fieldType != String.class && isPrimitiveType(fieldType)) {
							try {
								value = convertStringToPrimitiveType(fieldType, (String)value);
							} catch (Exception e) {
								throw new ConversionException(String.format("Can't convert string to %s.", type.getName()), e);
							}
						}
						
						try {
							validate(validatorAnnotations, value);
						} catch (ValidationException e) {
							throw new BadMessageException(String.format("Validation error: text only '%s'. Parsing context: %s.",
									value, context), e);
						}
						
						validateElement(context, value);
						
						try {
							pd.getWriteMethod().invoke(parent, new Object[] {value});
						} catch (Exception e) {
							throw new RuntimeException("Can't set text only value.", e);
						}
					}
					
				};
			}
			
			return null;
		}
		
		public void write(IParsingContext<?> context, Object parent, Object element) {
		}
	}
	
	private class TextOnlyArrayItemElementAccessor extends ArrayItemElementAccessor {
		private static final String NAME_DEFAULT_TEXT_OBJECT = "text";
		private Annotation converterAnnotation;
		private List<Annotation> validatorAnnotations;
		private Class<?> elementType;
		
		public TextOnlyArrayItemElementAccessor(Class<?> elementType, Annotation converterAnnotation,
				List<Annotation> validatorAnnotations, PropertyDescriptor ownAccessor) {
			super(null, ownAccessor);
			this.elementType = elementType;
			this.converterAnnotation = converterAnnotation;
			this.validatorAnnotations = validatorAnnotations;
		}
		
		@Override
		public String getTextObjectName() {
			return NAME_DEFAULT_TEXT_OBJECT;
		}
		
		@Override
		public IPropertyAccessor getPropertyAccessor(String name) {
			if (NAME_DEFAULT_TEXT_OBJECT.equals(name)) {
				return new IPropertyAccessor() {

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void write(IParsingContext<?> context, Object parsingObject, Object value) {
						Object array = null;
						try {
							array = ownAccessor.getReadMethod().invoke(parsingObject, new Object[] {});
						} catch (Exception e) {
							throw new RuntimeException(String.format("%s can't add element value to array.", arrayElementWritter.getName()), e);
						}
						
						if (array instanceof List) {
							
							try {
								value = convert(converterAnnotation, value);
							} catch (ConversionException e) {
								// impossible
							}
							
							if ((value instanceof String) && (elementType != String.class) && isPrimitiveType(elementType)) {
								try {
									value = convertStringToPrimitiveType(elementType, (String)value);
								} catch (Exception e) {
									throw new ConversionException(String.format("Can't convert string to %s.", elementType.getName()), e);
								}
							}
							
							try {
								validate(validatorAnnotations, value);
							} catch (ValidationException e) {
								throw new BadMessageException(String.format("Validation error: text only '%s'. Parsing context: %s.",
										value, context), e);
							}
							
							validateElement(context, value);
							
							((List)array).add(value);
						} else {
							throw new RuntimeException("Parent isn't a list!!!???");
						}
					}
					
				};
			}
			
			return null;
		}
		
		public void write(IParsingContext<?> context, Object parent, Object element) {
		}
	}
	
	private class NopElementAccessor implements IElementAccessor {

		@Override
		public void putPropertyAccessor(String name, IPropertyAccessor propertyAccessor) {}

		@Override
		public IPropertyAccessor getPropertyAccessor(String name) {
			return null;
		}

		@Override
		public void setTextObjectName(String textObjectName) {}

		@Override
		public String getTextObjectName() {
			return null;
		}

		@Override
		public Class<?> getType() {
			return null;
		}
		
		@Override
		public void write(IParsingContext<?> context, Object parent, Object element) {
		}

		@Override
		public Object create(IParsingContext<?> context) {
			return null;
		}

		@Override
		public Object get(IParsingContext<?> context) {
			return null;
		}

		@Override
		public Object remove(IParsingContext<?> context) {
			return null;
		}		
	}
	
	private static interface IPropertyAccessor {
		void write(IParsingContext<?> context, Object object, Object value) throws ConversionException, ValidationException;
	}
	
	
	private class DefaultPropertyAccessor implements IPropertyAccessor {
		protected PropertyDescriptor propertyDescriptor; 
		protected Annotation converterAnnotation;
		protected List<Annotation> validatorAnnotations;
		
		public DefaultPropertyAccessor(PropertyDescriptor propertyDescriptor, Annotation converterAnnotation,
				List<Annotation> validatorAnnotations) {
			this.propertyDescriptor = propertyDescriptor;
			this.converterAnnotation = converterAnnotation;
			this.validatorAnnotations = validatorAnnotations;
		}

		@Override
		public void write(IParsingContext<?> context, Object object, Object value)
					throws ConversionException, ValidationException {
			Class<?> type = propertyDescriptor.getPropertyType();
			
			try {
				value = convert(converterAnnotation, value);
			} catch(ConversionException e) {
				throw e;
			}
			
			if ((value instanceof String) && isBytes(type)) {
				value = BinaryUtils.decodeFromBase64((String)value);
			} else if ((value instanceof String) && isByte(type)) {
				value = BinaryUtils.getByte((String)value);
			} else if ((value instanceof String) && (type != String.class) && isPrimitiveType(type)) {
				try {
					value = convertStringToPrimitiveType(type, (String)value);
				} catch (Exception e) {
					throw new ConversionException(String.format("Can't convert string to %s.", type.getName()), e);
				}
			}
			
			try {
				validate(validatorAnnotations, value);
			} catch (ValidationException e) {
				throw new BadMessageException(String.format("Validation error: text only '%s'. Parsing context: %s.",
						value, context), e);
			}
			
			try {
				propertyDescriptor.getWriteMethod().invoke(object, new Object[] {value});
			} catch (Exception e) {
				throw new RuntimeException(String.format("Can't set property[%s.%s] value.",
						object.getClass().getName(), propertyDescriptor.getName()), e);
			}
		}
	}
	
	private Object convertStringToPrimitiveType(Class<?> type, String value) {
		if (type.equals(boolean.class) || type.equals(Boolean.class)) {
			return Boolean.valueOf(value);
		} else if (type.equals(byte.class) || type.equals(Byte.class)) {
			return Byte.valueOf(value);
		} else if (type.equals(int.class) || type.equals(Integer.class)) {
			return Integer.valueOf(value);
		} else if (type.equals(long.class) || type.equals(Long.class)) {
			return Long.valueOf(value);
		} else if (type.equals(float.class) || type.equals(Float.class)) {
			return Float.valueOf(value);
		} else if (type.equals(double.class) || type.equals(Double.class)) {
			return Double.valueOf(value);
		} else if (type.equals(BigInteger.class)) {
			return new BigInteger(value);
		} else if (type.equals(BigDecimal.class)) {
			return new BigDecimal(value);
		} else {
			return value;
		}
	}
	
	private Object convert(Annotation converterAnnotation, Object value) {
		if (converterAnnotation != null) {
			value = ConversionService.from(converterAnnotation, value);
		}
		
		return value;
	}
	
	private void validate(List<Annotation> validatorAnnotations, Object value) throws ValidationException {
		if (validatorAnnotations != null && validatorAnnotations.size() > 0) {
			for (Annotation validatorAnnotation : validatorAnnotations) {
				ValidationService.validate(validatorAnnotation, value);
			}
		}
	}
	
	/*private Object convertAndValidate(Annotation converterAnnotation, List<Annotation> validatorAnnotations,
				Object value) throws ValidationException {
		
		
		if (validatorAnnotations != null && validatorAnnotations.size() > 0) {
			for (Annotation validatorAnnotation : validatorAnnotations) {
				ValidationService.validate(validatorAnnotation, value);
			}
		}
		
		return value;
	}*/
}
