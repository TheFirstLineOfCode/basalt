package com.thefirstlineofcode.basalt.oxm.parsers.error;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.parsing.ElementParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.oxm.parsing.ParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingUtils;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public abstract class ErrorDetailsParserFactory implements IParserFactory<Object> {
	private static final String PATH_ROOT = "/";
	
	private static final RootElementParser rootElementParser = new RootElementParser();

	@Override
	public IParser<Object> create() {
		return new ErrorDetailsParser();
	}
	
	public class ErrorDetailsParser extends ParserAdaptor<Object> {
		public ErrorDetailsParser() {
			super(null);
		}
		
		@Override
		public Object createObject() {
			return null;
		}
		
		@Override
		public IElementParser<Object> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match(PATH_ROOT)) {
				return rootElementParser;
			}

			return null;
		} 
		
	}
	
	private static class RootElementParser extends ElementParserAdaptor<Object> {
		private static final String PROTOCOL_TEXT_LOCAL_NAME = "text";
		
		@Override
		public void processAttributes(IParsingContext<Object> context, List<Attribute> attributes) {
			Protocol protocol = context.getProtocolChain().get(context.getProtocolChain().size() - 1);
			
			if (PROTOCOL_TEXT_LOCAL_NAME.equals(protocol.getLocalName())) {
				String lang = ParsingUtils.processLangTextAttributes(attributes);
				((ParsingContext<Object>)context).setObject(new LangText(null, lang));
			} else {
				((ParsingContext<Object>)context).setObject(protocol.getLocalName());
			}
		}
		
		@Override
		public void processText(IParsingContext<Object> context, Value<?> text) {
			Protocol protocol = context.getProtocolChain().get(context.getProtocolChain().size() - 1);
			
			if (PROTOCOL_TEXT_LOCAL_NAME.equals(protocol.getLocalName())) {
				((LangText)(context.getObject())).setText(text.toString());
			} else {
				if (text != null) {
					throw new BadMessageException(String.format("Text not allowed in here. Protocol %s.", context.getProtocolChain()));
				}
			}
		}
	}
}
