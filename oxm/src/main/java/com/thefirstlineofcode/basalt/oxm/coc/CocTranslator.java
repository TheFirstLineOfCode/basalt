package com.thefirstlineofcode.basalt.oxm.coc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attributes;
import com.thefirstlineofcode.basalt.oxm.Value;
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
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class CocTranslator<T> implements ITranslator<T> {
	private Protocol protocol;
	private ProtocolDescriptor descriptor;
	
	public CocTranslator(Class<T> type) {
		this(getProtocol(type), type);
	}

	private CocTranslator(Protocol protocol, Class<T> type) {
		this.protocol = protocol;
		descriptor = getProtocolDescriptor(type, protocol);
	}
	
	private static Protocol getProtocol(Class<?> type) {
		ProtocolObject protocolObject = type.getAnnotation(ProtocolObject.class);
		if (protocolObject == null) {
			throw new IllegalArgumentException("@ProtocolObject not found.");
		}
		
		return new Protocol(protocolObject.namespace(), protocolObject.localName());
	}

	private ProtocolDescriptor getProtocolDescriptor(Class<?> type, Protocol protocol) {
		ProtocolDescriptor descriptor = new ProtocolDescriptor();
		descriptor.protocol = protocol;
		
		for (Field field : type.getDeclaredFields()) {
			PropertyDescriptor propertyDescriptor = null;
			try {
				propertyDescriptor = new PropertyDescriptor(field.getName(), type);
			} catch (IntrospectionException e) {
				continue;
			}
			
			if (propertyDescriptor.getReadMethod() == null)
				continue;
			
			ProtocolObject embeddedObject = field.getType().getAnnotation(ProtocolObject.class);
			
			if (embeddedObject != null) {
				addEmbeddedProtocolDescriptor(descriptor, propertyDescriptor, field.getType());
			} else {
				assembleElementDescriptor(descriptor, field, propertyDescriptor);
			}
		}
		
		return descriptor;
	}
	
	private void addEmbeddedProtocolDescriptor(ProtocolDescriptor descriptor,
			PropertyDescriptor propertyDescriptor, Class<?> type) {
		ProtocolObject embeddedObject = type.getAnnotation(ProtocolObject.class);
		if (embeddedObject == null) {
			throw new RuntimeException("Can't determine embedded protocol.");
		}
		
		ProtocolDescriptor embeddedProtocolDescriptor = getProtocolDescriptor(type,
				new Protocol("".equals(embeddedObject.namespace()) ? null : embeddedObject.namespace(), embeddedObject.localName()));
		embeddedProtocolDescriptor.propertyDescriptor = propertyDescriptor;
		descriptor.protocols.add(embeddedProtocolDescriptor);
	}
	
	private ElementDescriptor getElementDescriptor(Class<?> type) {
		ElementDescriptor descriptor = new ElementDescriptor();
		
		for (Field field : type.getDeclaredFields()) {
			PropertyDescriptor propertyDescriptor = null;
			try {
				propertyDescriptor = new PropertyDescriptor(field.getName(), type);
			} catch (IntrospectionException e) {
				continue;
			}
			
			if (propertyDescriptor.getReadMethod() == null)
				continue;
			
			assembleElementDescriptor(descriptor, field, propertyDescriptor);
		}
		
		return descriptor;
	}
	
	private void assembleElementDescriptor(ElementDescriptor descriptor, Field field,
			PropertyDescriptor propertyDescriptor) {
		Text text = null;
		BindTo bindTo = null;
		Array array = null;
		TextOnly textOnly = null;
		BooleanOnly booleanOnly = null;
		Annotation converterAnnotation = null;
		
		Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			Class<?> annotationType = annotation.annotationType();
			if (annotationType == NotBind.class)
				return;
			
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
			}
		}
		
		if (array != null) {
			ArrayDescriptor arrayDescriptor = new ArrayDescriptor();
			Class<?> elementType = array.value();
			
			if (textOnly != null && !isPrimitiveType(elementType) && converterAnnotation == null) {
				throw new RuntimeException("Text only element must be primitive type or have a converter.");
			}
			
			String arrayElementName = getArrayElementName(array.elementName(), elementType);
			if (arrayElementName == null) {
				throw new RuntimeException(String.format("Null array element name"));
			}
			
			arrayDescriptor.name = getTranslatingName(field, bindTo);
			arrayDescriptor.propertyDescriptor = propertyDescriptor;
			if (textOnly != null) {
				TextOnlyDescriptor textOnlyDescriptor = new TextOnlyDescriptor();
				textOnlyDescriptor.name = arrayElementName;
				textOnlyDescriptor.converterAnnotation = converterAnnotation;
				
				arrayDescriptor.textOnly = textOnlyDescriptor;
			} else {
				ElementDescriptor elementDescriptor = getElementDescriptor(elementType);
				elementDescriptor.name = arrayElementName;
				
				arrayDescriptor.element = elementDescriptor;
			}
			
			descriptor.arrays.add(arrayDescriptor);
		} else if (textOnly != null) {
			TextOnlyDescriptor textOnlyDescriptor = new TextOnlyDescriptor();
			textOnlyDescriptor.name = getTranslatingName(field, bindTo);
			textOnlyDescriptor.propertyDescriptor = propertyDescriptor;
			textOnlyDescriptor.converterAnnotation = converterAnnotation;
			
			descriptor.textOnlys.add(textOnlyDescriptor);
		} else if (booleanOnly != null) {
			BooleanOnlyDescriptor booleanOnlyDescriptor = new BooleanOnlyDescriptor();
			booleanOnlyDescriptor.name = getTranslatingName(field, bindTo);
			booleanOnlyDescriptor.propertyDescriptor = propertyDescriptor;
			
			descriptor.booleanOnlys.add(booleanOnlyDescriptor);
		} else if (text != null) {
			TextDescriptor textDescriptor = new TextDescriptor();
			textDescriptor.name = getTranslatingName(field, bindTo);
			textDescriptor.propertyDescriptor = propertyDescriptor;
			textDescriptor.converterAnnotation = converterAnnotation;
			textDescriptor.CData = text.CDATA();
			
			descriptor.text = textDescriptor;
		} else if (isBytes(field.getType()) || isByte(field.getType()) || isPrimitiveType(field.getType()) || converterAnnotation != null) {
			AttributeDescriptor attributeDescriptor = new AttributeDescriptor();
			attributeDescriptor.name = getTranslatingName(field, bindTo);
			attributeDescriptor.propertyDescriptor = propertyDescriptor;
			attributeDescriptor.converterAnnotation = converterAnnotation;
			
			descriptor.attributes.add(attributeDescriptor);
		} else {
			ElementDescriptor elementDescriptor = getElementDescriptor(field.getType());
			elementDescriptor.name = getTranslatingName(field, bindTo);
			elementDescriptor.propertyDescriptor = propertyDescriptor;
			
			descriptor.elements.add(elementDescriptor);
		}
	}

	private boolean isBytes(Class<?> type) {
		return type == byte[].class;
	}
	
	private boolean isByte(Class<?> type) {
		return type == byte.class || type == Byte.class;
	}

	private String getArrayElementName(String elementName, Class<?> elementType) {
		if (elementName != null && !elementName.isEmpty())
			return elementName;
		
		if (isPrimitiveType(elementType)) {
			throw new IllegalArgumentException("Element type is primitive type. Element name should be set explicitly.");
		}
		
		return getNameByNamingConvention(elementType.getSimpleName());
	}

	private String getTranslatingName(Field field, BindTo bindTo) {
		if (bindTo != null) {
			return bindTo.value();
		}else {
			return getNameByNamingConvention(field.getName());
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

	private boolean isPrimitiveType(Class<?> type) {
		return type.equals(String.class) ||
				type.equals(boolean.class) ||
				type.equals(Boolean.class) ||
				type.equals(int.class) ||
				type.equals(Integer.class) ||
				type.equals(long.class) ||
				type.equals(Long.class) ||
				type.equals(float.class) ||
				type.equals(Float.class) ||
				type.equals(double.class) ||
				type.equals(Double.class) ||
				type.equals(BigInteger.class) ||
				type.equals(BigDecimal.class);
	}

	@Override
	public String translate(T object, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
		translate(descriptor, object, writer, translatingFactory);
		
		return writer.getDocument();
	}
	
	private void translate(ProtocolDescriptor descriptor, Object object, IProtocolWriter writer,
				ITranslatingFactory translatingFactory) {
		if (object == null)
			return;
		
		writer.writeProtocolBegin(descriptor.protocol);
		
		translate(descriptor, object, writer);
		
		if (descriptor.protocols.size() > 0) {
			for (ProtocolDescriptor protocolDescriptor : descriptor.protocols) {
				Object embedded = getValue(protocolDescriptor.propertyDescriptor, object);
				if (embedded == null)
					continue;
				
				String embeddedString = translatingFactory.translate(embedded);
				
				writer.writeString(embeddedString);
			}
		}
		
		writer.writeProtocolEnd();
	}

	private void translate(ElementDescriptor descriptor, Object object, IProtocolWriter writer) {
		if (object == null)
			return;
		
		if (descriptor.attributes.size() > 0) {
			Attributes attributes = new Attributes();
			for (AttributeDescriptor attributeDescriptor : descriptor.attributes) {
				Value<?> value = getValue(attributeDescriptor.propertyDescriptor,
						attributeDescriptor.converterAnnotation, object);
				
				attributes.add(attributeDescriptor.name, value);
			}
			
			if (attributes.size() > 0) {
				writer.writeAttributes(attributes.get());
			}
		}
		
		if (descriptor.text != null) {
			Value<?> vText = getValue(descriptor.text.propertyDescriptor, descriptor.text.
					converterAnnotation, object);
			if (vText != null) {
				String text = vText.getString();
				if (descriptor.text.CData) {
					writer.writeString("<![CDATA[");
					writer.writeString(text);
					writer.writeString("]]>");
				} else {				
					writer.writeText(text);
				}	
			}
		}
		
		if (descriptor.textOnlys.size() > 0) {
			for (TextOnlyDescriptor textOnlyDescriptor : descriptor.textOnlys) {
				Value<?> value = getValue(textOnlyDescriptor.propertyDescriptor,
						textOnlyDescriptor.converterAnnotation, object);
				writer.writeTextOnly(textOnlyDescriptor.name, value);
			}
		}
		
		if (descriptor.booleanOnlys.size() > 0) {
			for (BooleanOnlyDescriptor booleanOnlyDescriptor : descriptor.booleanOnlys) {
				boolean b = (Boolean)getValue(booleanOnlyDescriptor.propertyDescriptor, object);
				if (b) {
					writer.writeElementBegin(booleanOnlyDescriptor.name);
					writer.writeElementEnd();
				}
			}
		}
		
		if (descriptor.elements.size() > 0) {
			for (ElementDescriptor elementDescriptor : descriptor.elements) {
				Object element = getValue(elementDescriptor.propertyDescriptor, object);
				if (element == null)
					continue;
				
				writer.writeElementBegin(elementDescriptor.name);
				translate(elementDescriptor, element, writer);
				writer.writeElementEnd();
			}
		}
		
		if (descriptor.arrays.size() > 0) {
			for (ArrayDescriptor arrayDescriptor : descriptor.arrays) {
				Object array = getValue(arrayDescriptor.propertyDescriptor, object);
				
				if (array == null)
					continue;
				
				if (!(array instanceof List)) {
					throw new RuntimeException("Array object doesn't implement List interface.");
				}
				
				List<?> list = (List<?>)array;
				if (list.size() == 0)
					continue;
				
				for (Object arrayElement : list) {
					if (arrayDescriptor.textOnly != null) {
						writer.writeTextOnly(arrayDescriptor.textOnly.name, Value.createValue(arrayElement));
					} else {
						writer.writeElementBegin(arrayDescriptor.element.name);
						translate(arrayDescriptor.element, arrayElement, writer);
						writer.writeElementEnd();
					}
				}
			}
		}		
	}
	
	private Object getValue(PropertyDescriptor propertyDescriptor, Object object) {
		try {
			return propertyDescriptor.getReadMethod().invoke(object, new Object[0]);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Can't invoke get value method %s.", propertyDescriptor.getReadMethod()));
		}
	}

	private Value<?> getValue(PropertyDescriptor propertyDescriptor,
			Annotation converterAnnotation, Object object) {
		Object value = getValue(propertyDescriptor, object);
		
		if (value == null)
			return null;
		
		if (converterAnnotation != null) {
			value = ConversionService.to(converterAnnotation, value);
		}
		
		if (value == null)
			return null;
		
		return Value.createValue(value);
	}

	@Override
	public Protocol getProtocol() {
		return protocol;
	}
	
	private class TranslatingObjectDescriptor {
		public String name;
		public PropertyDescriptor propertyDescriptor;
	}
	
	private class ProtocolDescriptor extends ElementDescriptor {
		public Protocol protocol;
		public List<ProtocolDescriptor> protocols = new ArrayList<>();
	}
	
	private class ElementDescriptor extends TranslatingObjectDescriptor {
		public List<AttributeDescriptor> attributes = new ArrayList<>();;
		public List<ElementDescriptor> elements = new ArrayList<>();
		public List<TextOnlyDescriptor> textOnlys = new ArrayList<>();
		public List<BooleanOnlyDescriptor> booleanOnlys = new ArrayList<>();
		public List<ArrayDescriptor> arrays = new ArrayList<>();
		public TextDescriptor text;
	}
	
	private class AttributeDescriptor extends TranslatingObjectDescriptor {
		public Annotation converterAnnotation;
	}
	
	private class TextDescriptor extends TranslatingObjectDescriptor {
		public Annotation converterAnnotation;
		public boolean CData;
	}
	
	private class ArrayDescriptor extends TranslatingObjectDescriptor {
		public ElementDescriptor element;
		public TextOnlyDescriptor textOnly;
	}
	
	private class TextOnlyDescriptor extends TranslatingObjectDescriptor {
		public Annotation converterAnnotation;
	}
	
	private class BooleanOnlyDescriptor extends TranslatingObjectDescriptor {}

}
