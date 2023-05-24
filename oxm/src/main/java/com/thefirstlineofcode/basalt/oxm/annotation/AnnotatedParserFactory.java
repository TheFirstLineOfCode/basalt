package com.thefirstlineofcode.basalt.oxm.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessAttributes;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessEmbeddedObject;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessText;
import com.thefirstlineofcode.basalt.oxm.parsing.ElementParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class AnnotatedParserFactory<T> implements IParserFactory<T> {
	private Object annotatedParser;
	private Protocol protocol;
	private Class<T> objectType;
	private static final String NULL_NAMESPACE = "";
	private Map<String, IElementParser<T>> elementParsers;
	private Method processEmbeddedObjectMethod;
	private boolean stateless;
	private AnnotatedParser parser;
	
	@SuppressWarnings("unchecked")
	public AnnotatedParserFactory(Class<?> annotatedParserType) {
		Parser parserAnnotation = annotatedParserType.getAnnotation(Parser.class);
		if (parserAnnotation == null) {
			throw new IllegalArgumentException("No @Parser found.");
		}
		
		stateless = parserAnnotation.stateless();
		String namespace = parserAnnotation.namespace();
		protocol = new Protocol(NULL_NAMESPACE.equals(namespace) ? null : namespace, parserAnnotation.localName());
		
		objectType = (Class<T>)parserAnnotation.objectType();
		
		Method[] methods = annotatedParserType.getMethods();
		elementParsers = assembleElementParsers(methods);
		scanProcessEmbeddedObjectMethod(methods);
		
		try {
			annotatedParser = annotatedParserType.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can't initiate annoated parser.", e);
		}
		
		if (stateless) {
			parser = new AnnotatedParser();
		}
	}
	
	private void scanProcessEmbeddedObjectMethod(Method[] methods) {
		for (Method method : methods) {
			ProcessEmbeddedObject processEmbeddedObject = method.getAnnotation(ProcessEmbeddedObject.class);
			if (processEmbeddedObject != null) {
				if (!checkProcessEmbeddedObjectMethodSigunature(method)) {
					throw new IllegalArgumentException("Illegal process embedded object method. " +
						"Method sigunature should be void processEmbeddedObject(IParsingContext<T> context, Protocol protocol, Object object).");
				}
				
				processEmbeddedObjectMethod = method;
			}
		}
	}

	public boolean getStateless() {
		return stateless;
	}
	
	private Map<String, IElementParser<T>> assembleElementParsers(Method[] methods) {
		Map<String, IElementParser<T>> elementParsers = new HashMap<>();
		Map<String, Method> processTextMethods = new HashMap<>();
		Map<String, Method> processAttributesMethods = new HashMap<>();
		List<String> paths = new ArrayList<>();
		
		for (Method method : methods) {
			if (method.getDeclaringClass() == Object.class)
				continue;
			
			ProcessAttributes processAttributes = method.getAnnotation(ProcessAttributes.class);
			if (processAttributes != null) {
				if (!checkProcessAttributesMethodSignature(method)) {
					throw new IllegalArgumentException("Illegal process attributes method sigunature. " +
							"Method sigunature should be void processAttributes(IParsingContext<T> context, List<Attribute> attributes).");
				}
				
				String path = processAttributes.value();
				processAttributesMethods.put(path, method);
				if (!paths.contains(path)) {
					paths.add(path);
				}
				
				continue;
			}
			
			ProcessText processText = method.getAnnotation(ProcessText.class);
			if (processText != null) {
				if (!checkProcessTextMethodSigunature(method)) {
					throw new IllegalArgumentException("Illegal process text method sigunature. " +
						"Method sigunature should be void processText(IParsingContext<T> context, Value<?> text).");
				}
				
				String path = processText.value();
				processTextMethods.put(path, method);
				if (!paths.contains(path)) {
					paths.add(path);
				}
			}
		}
		
		for (String path : paths) {
			Method processAttributesMethod = processAttributesMethods.get(path);
			Method processTextMethod = processTextMethods.get(path);
			
			AnnotatedElementParser elementParser = new AnnotatedElementParser(
					processAttributesMethod, processTextMethod);
			elementParsers.put(path, elementParser);
		}
		
		return elementParsers;
	}
	
	private boolean checkProcessTextMethodSigunature(Method method) {
		if (method.getReturnType() != void.class) {
			return false;
		}
		
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length != 2)
			return false;
		
		if (!paramTypes[0].equals(IParsingContext.class)) {
			return false;
		}
		
		if (!paramTypes[1].equals(Value.class)) {
			return false;
		}
		
		return true;
	}
	
	private boolean checkProcessAttributesMethodSignature(Method method) {
		if (method.getReturnType() != void.class) {
			return false;
		}
		
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length != 2)
			return false;
		
		if (!paramTypes[0].equals(IParsingContext.class)) {
			return false;
		}
		
		if (!paramTypes[1].equals(List.class)) {
			return false;
		}
		
		return true;
	}
	
	private boolean checkProcessEmbeddedObjectMethodSigunature(Method method) {
		if (method.getReturnType() != void.class) {
			return false;
		}
		
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length != 3)
			return false;
		
		if (!paramTypes[0].equals(IParsingContext.class)) {
			return false;
		}
		
		if (!paramTypes[1].equals(Protocol.class)) {
			return false;
		}
		
		if (!paramTypes[2].equals(Object.class)) {
			return false;
		}
		
		return true;
	}

	@Override
	public Protocol getProtocol() {
		return protocol;
	}
	
	private class AnnotatedElementParser extends ElementParserAdaptor<T> {
		private Method processAttributesMethod;
		private Method processTextMethod;
		
		public AnnotatedElementParser(Method processAttributesMethod, Method processTextMethod) {
			this.processAttributesMethod = processAttributesMethod;
			this.processTextMethod = processTextMethod;
		}
		
		@Override
		public void processText(IParsingContext<T> context, Value<?> text) {
			if (processTextMethod != null) {
				try {
					processTextMethod.invoke(annotatedParser, new Object[] {context, text});
				} catch (Exception e) {
					throw new RuntimeException("Can't invoke process text method.", e);
				}
			} else {
				super.processText(context, text);
			}
		}

		@Override
		public void processAttributes(IParsingContext<T> context, List<Attribute> attributes) {
			if (processAttributesMethod != null) {
				try {
					processAttributesMethod.invoke(annotatedParser, new Object[] {context, attributes});
				} catch (Exception e) {
					throw new RuntimeException("Can't invoke process attributes method.", e);
				}
			} else {
				super.processAttributes(context, attributes);
			}
		}
	}
	
	@Override
	public IParser<T> create() {
		if (stateless) {
			return parser;
		} else {
			return new AnnotatedParser();
		}
	}
	
	private class AnnotatedParser implements IParser<T> {
		
		@Override
		public T createObject() {
			try {
				return objectType.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Can't create protocol object.", e);
			}
		}

		@Override
		public IElementParser<T> getElementParser(IParsingPath parsingPath) {
			IElementParser<T> parser = elementParsers.get(parsingPath.toString());
			
			if (parser == null) {
				String sParsingPath = parsingPath.toString();
				for (String explicit : elementParsers.keySet()) {
					if (explicit.startsWith(sParsingPath)) {
						parser = new ElementParserAdaptor<>();
						break;
					}
				}
				
				if (processEmbeddedObjectMethod != null) {
					parser = new ElementParserAdaptor<>();
				}
			}
			
			return parser;
		}

		@Override
		public void processEmbeddedObject(IParsingContext<T> context, Protocol protocol, Object embedded) {
			if (processEmbeddedObjectMethod != null) {
				try {
					processEmbeddedObjectMethod.invoke(annotatedParser, new Object[] {context, protocol, embedded});
				} catch (Exception e) {
					throw new RuntimeException("Can't invoke process embedded object method.", e);
				}
			}
		}
	}
}
