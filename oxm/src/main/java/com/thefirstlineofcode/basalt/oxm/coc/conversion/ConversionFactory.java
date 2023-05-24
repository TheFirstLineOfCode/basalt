package com.thefirstlineofcode.basalt.oxm.coc.conversion;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;

public class ConversionFactory implements IConversionFactory {
	private ConcurrentMap<Class<? extends Annotation>, IConverterFactory> converterFactories;
	
	public ConversionFactory() {
		this.converterFactories = new ConcurrentHashMap<>();
	}

	@Override
	public IConverter<?, ?> getConverter(Annotation annotation) {
		IConverterFactory factory = (IConverterFactory)converterFactories.get(annotation.annotationType());
		
		if (factory != null)
			return factory.create(annotation);
		
		ConverterFactory factoryAnnotation = annotation.annotationType().getAnnotation(ConverterFactory.class);
		if (factoryAnnotation != null) {
			try {
				factory = (IConverterFactory)factoryAnnotation.value().getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Can't create converter factory.", e);
			}
		}
		
		if (factory == null) {
			Converter converterAnnotation = annotation.annotationType().getAnnotation(Converter.class);
			if (converterAnnotation == null) {
				throw new RuntimeException("???Can't get converter annotation???");
			}
			
			factory = new DefaultConverterFactory(converterAnnotation.value());
		}
		
		IConverterFactory old = converterFactories.putIfAbsent(annotation.annotationType(), factory);
		if (old != null) {
			factory = old;
		}
		
		return factory.create(annotation);
	}
	
	private class DefaultConverterFactory implements IConverterFactory {
		private Class<? extends IConverter<?, ?>> converterType;
		
		public DefaultConverterFactory(Class<? extends IConverter<?, ?>> converterType) {
			this.converterType = converterType;
		}

		@Override
		public IConverter<?, ?> create(Annotation annotation) {
			try {
				return converterType.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Can't create converter instance.", e);
			}
		}
		
	}

}
