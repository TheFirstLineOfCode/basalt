package com.thefirstlineofcode.basalt.oxm;

import java.util.HashMap;
import java.util.Map;

import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;

public class TranslatingFactory implements ITranslatingFactory {
	private IProtocolWriterFactory writerFactory;
	private Map<Class<?>, ITranslatorFactory<?>> factories;
	
	public TranslatingFactory(IProtocolWriterFactory writerFactory) {
		this.writerFactory = writerFactory;
		factories = new HashMap<>();
	}

	@Override
	public String translate(Object object) {
		ITranslatorFactory<?> factory = findTranslatorFactory(object.getClass());
		if (factory == null) {
			throw new IllegalArgumentException(String.format("Translating %s isn't supported.", object.getClass()));
		}
		
		IProtocolWriter writer = writerFactory.create();
		return translate(object, writer, factory, this);
	}
	
	@SuppressWarnings("unchecked")
	private <T> String translate(Object object, IProtocolWriter writer, ITranslatorFactory<T> translatorFactory,
				ITranslatingFactory translatingFactory){
		ITranslator<T> translator = translatorFactory.create();
		return translator.translate((T)object, writer, translatingFactory);
	}

	private ITranslatorFactory<?> findTranslatorFactory(Class<?> clazz) {
		if (clazz == Object.class)
			return null;
		
		ITranslatorFactory<?> factory = factories.get(clazz);
		if (factory != null) {
			return factory;
		}
		
		Class<?> superClass = clazz.getSuperclass();
		while (superClass != null && superClass != Object.class) {
			factory = factories.get(superClass);
			
			if (factory != null)
				return factory;
			
			superClass = superClass.getSuperclass();
		}
		
		Class<?>[] intfs = clazz.getInterfaces();
		
		for (Class<?> intf : intfs) {
			factory = findTranslatorFactory(intf);
			
			if (factory != null)
				return factory;
		}
		
		return factory;
	}

	@Override
	public void register(Class<?> type, ITranslatorFactory<?> translatorFactory) {
		factories.put(type, translatorFactory);
	}

	@Override
	public void unregister(Class<?> type) {
		factories.remove(type);
	}
	
	@Override
	public boolean isRegistered(Class<?> type) {
		return factories.containsKey(type);
	}

	@Override
	public IProtocolWriterFactory getWriterFactory() {
		return writerFactory;
	}

}
