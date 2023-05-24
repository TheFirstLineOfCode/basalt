package com.thefirstlineofcode.basalt.oxm.parsers.core.stream.sasl;

import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsing.ElementParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.oxm.parsing.ParserAdaptor;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.BadFormat;
import com.thefirstlineofcode.basalt.xmpp.core.stream.sasl.Failure;

public class FailureParserFactory implements IParserFactory<Failure> {
	private static final IParser<Failure> parser = new FailureParser();
	
	@Override
	public Protocol getProtocol() {
		return Failure.PROTOCOL;
	}
	
	@Override
	public IParser<Failure> create() {
		return parser;
	}
	
	private static class FailureParser extends ParserAdaptor<Failure> {
		
		public FailureParser() {
			super(null);
		}
		
		@Override
		public Failure createObject() {
			return new Failure();
		}
		
		@Override
		public IElementParser<Failure> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match("/"))
				return new ElementParserAdaptor<Failure>() {
				@Override
				public void processText(IParsingContext<Failure> context, Value<?> text) {
					if (context.getObject().getErrorCondition() == null)
						throw new ProtocolException(new BadFormat(String.format("Null error condition[%s].", context.getProtocolChain())));
				}
			};
			
			String elementName = parsingPath.toString().substring(1);
			
			if (elementName.indexOf('/') != -1) {
				throw new ProtocolException(new BadFormat(String.format("Illegal parsing path %s[PROTOCOL: %s].",
						parsingPath, Failure.PROTOCOL)));
			}
			
			
			final Failure.ErrorCondition errorCondition = getErrorConditionEnum(elementName);
			if (errorCondition != null) {
				return new ElementParserAdaptor<Failure>() {
					@Override
					public void processText(IParsingContext<Failure> context, Value<?> text) {
						super.processText(context, text);
						
						if (context.getObject().getErrorCondition() != null) {
							throw new ProtocolException(new BadFormat("Only one error condition allowed[PROTOCOL: %s]."));
						}
						
						context.getObject().setErrorCondition(errorCondition);
					}
				};
			} else {
				throw new ProtocolException(new BadFormat(String.format("Invalid failure error condition %s[PROTOCOL: %s].",
						elementName, Failure.PROTOCOL)));
			}
			
		}

		private Failure.ErrorCondition getErrorConditionEnum(String elementName) {
			for (Failure.ErrorCondition errorCondition : Failure.ErrorCondition.values()) {
				if (errorCondition.toString().toLowerCase().replaceAll("_", "-").equals(elementName))
					return errorCondition;
			}
			
			return null;
		}
		
	}
	
}
