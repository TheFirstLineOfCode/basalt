package com.thefirstlineofcode.basalt.oxm.xml;

import java.util.List;
import java.util.Stack;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.binary.BinaryUtils;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;

public class XmlProtocolWriter implements IProtocolWriter {
	private StringBuilder buffer = new StringBuilder();
	private Stack<Context> contexts = new Stack<>();
	
	private static class Context {
		public enum State {
			EMPTY,
			ATTRIBUTES_HAS_WRITTEN,
			TEXT_HAS_WRITTEN,
			SUB_ELEMENT_HAS_WRITTEN
		}
		
		public enum Type {
			PROTOCOL_ELEMENT,
			NORMAL_ELEMENT
		}
		
		public Type type;
		public State state;
		public Object data;
		
		public Context(Type type, Object data) {
			this.type = type;
			this.data = data;
			state = State.EMPTY;
			
		}
	}
	
	@Override
	public IProtocolWriter writeString(String string) {
		closeStartTagIfNeed(Context.State.SUB_ELEMENT_HAS_WRITTEN);
		
		buffer.append(string);		
		return this;
	}

	private void closeStartTagIfNeed(Context.State state) {
		if (!contexts.isEmpty()) {
			Context context = contexts.peek();
			if (context.state == Context.State.EMPTY || context.state == Context.State.ATTRIBUTES_HAS_WRITTEN) {
				buffer.append('>');
			}
			
			context.state = state;
		}
	}
	
	@Override
	public IProtocolWriter writeProtocolBegin(Protocol protocol) {
		closeStartTagIfNeed(Context.State.SUB_ELEMENT_HAS_WRITTEN);
		
		buffer.append('<');
		if (Stream.PROTOCOL.getNamespace().equals(protocol.getNamespace())) {
			if (!protocol.getLocalName().startsWith("stream:"))
				buffer.append("stream:");
			buffer.append(protocol.getLocalName());
			buffer.append(' ').append("xmlns:stream=\"").append(Stream.PROTOCOL.getNamespace()).append("\"");
		} else {
			buffer.append(protocol.getLocalName());
			
			if (protocol.getNamespace() != null) {
				buffer.append(" xmlns=").
					append('"').
					append(protocol.getNamespace()).
					append('"');
			}
		}
		
		contexts.push(new Context(Context.Type.PROTOCOL_ELEMENT, protocol));
		
		return this;
	}
	
	@Override
	public IProtocolWriter writeProtocolEnd() {
		Context context = contexts.pop();
		
		if (context.type != Context.Type.PROTOCOL_ELEMENT) {
			throw new IllegalStateException("current context isn't protocol element");
		}
		
		if (context.state == Context.State.EMPTY || context.state == Context.State.ATTRIBUTES_HAS_WRITTEN) {
			buffer.append('/').append('>');
		} else {
			Protocol protocol = (Protocol)context.data;
			
			buffer.append('<').
				append('/');
			
			if (Stream.PROTOCOL.getNamespace().equals(protocol.getNamespace()) && !protocol.getLocalName().startsWith("stream:")) {
				buffer.append("stream:");
			}
			
			buffer.append(protocol.getLocalName()).
				append('>');
		}
		
		return this;
	}

	@Override
	public IProtocolWriter writeElementBegin(String prefix, String localName) {
		if (contexts.isEmpty()) {
			throw new IllegalStateException("Normal element mustn't be at the top level.");
		}
		
		closeStartTagIfNeed(Context.State.SUB_ELEMENT_HAS_WRITTEN);
		
		buffer.append('<');
		if (prefix != null) {
			buffer.append(prefix).append(':');
		}
		
		buffer.append(localName);
		
		String elementName = prefix == null ? localName : String.format("%s:%s", prefix, localName);
		contexts.push(new Context(Context.Type.NORMAL_ELEMENT, elementName));
		
		return this;
	}

	@Override
	public IProtocolWriter writeElementBegin(String localName) {
		return writeElementBegin(null, localName);
	}

	@Override
	public IProtocolWriter writeElementEnd() {
		if (contexts.size() <= 1) {
			throw new IllegalStateException("Current context isn't normal element.");
		}
		
		Context context = contexts.pop();
		
		if (context.type != Context.Type.NORMAL_ELEMENT) {
			throw new IllegalStateException("Current context isn't normal element.");
		}
		
		if (context.state == Context.State.EMPTY || context.state == Context.State.ATTRIBUTES_HAS_WRITTEN) {
			buffer.append('/').append('>');
		} else {
			String elementName = (String)context.data;
			buffer.append('<').append('/');
			buffer.append(elementName).append('>');
		}
		
		return this;
	}

	@Override
	public IProtocolWriter writeEmptyElement(String prefix, String localName) {
		if (contexts.isEmpty()) {
			throw new IllegalStateException("Normal element mustn't be at the top level.");
		}
		
		closeStartTagIfNeed(Context.State.SUB_ELEMENT_HAS_WRITTEN);
		
		buffer.append('<');
		if (prefix != null) {
			buffer.append(prefix).append(':');
		}
		
		buffer.append(localName).append('/').append('>');
		
		return this;
	}

	@Override
	public IProtocolWriter writeEmptyElement(String localName) {
		return writeEmptyElement(null, localName);
	}

	@Override
	public IProtocolWriter writeAttributes(List<Attribute> attributes) {
		buffer.append(' ');
		for (Attribute attribute : attributes) {
			if (attribute.getValue() == null)
				continue;
			
			if (attribute.getPrefix() != null) {
				buffer.append(attribute.getPrefix()).append(':');
			}
			
			buffer.append(attribute.getLocalName()).append('=').append('"');
			if (attribute.getValue().getType() == Value.Type.BYTES) {
				buffer.append(escape(BinaryUtils.encodeToBase64(attribute.getValue().getBytes()))).append('"').append(' ');
			} else if (attribute.getValue().getType() == Value.Type.BYTE) {
				buffer.append(escape(BinaryUtils.getByteText(attribute.getValue().getByte()))).append('"').append(' ');				
			} else {
				buffer.append(escape(attribute.getValue().toString())).append('"').append(' ');
			}
		}
		
		if (buffer.charAt(buffer.length() - 1) == ' ') {
			buffer.delete(buffer.length() - 1, buffer.length());
		}
		
		contexts.peek().state = Context.State.ATTRIBUTES_HAS_WRITTEN;
		
		return this;
	}

	private String escape(String string) {
		char[] chars = string.toCharArray();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			
			switch (c) {
			case '&':
				if (!isEscapedChars(chars, i)) {
					sb.append("&amp;");
				} else {
					sb.append(c);
				}
				
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				sb.append(c);
			}
		}
		
		return sb.toString();
	}

	private boolean isEscapedChars(char[] chars, int position) {
		String[] escapedChars = new String[] {"&lt;", "&gt;", "&amp;", "&quot;", "&apos;"};
		
		topLoop:
		for (int i = 0; i < escapedChars.length; i++) {
			if (chars.length < position + escapedChars[i].length()) {
				continue;
			}
			
			for (int j = 0; j < escapedChars[i].length(); j++) {
				if (escapedChars[i].charAt(j) != chars[position + j]) {
					continue topLoop;
				}
			}
			
			return true;
			
		}
		
		return false;
	}

	@Override
	public IProtocolWriter writeText(Value<?> value) {
		return writeText(value.getString());
	}

	@Override
	public IProtocolWriter writeText(String value) {
		closeStartTagIfNeed(Context.State.TEXT_HAS_WRITTEN);
		
		if (value != null) {
			buffer.append(escape(value));
		}
		
		return this;
	}

	@Override
	public IProtocolWriter writeTextOnly(String prefix, String localName, Value<?> value) {
		return writeTextOnly(prefix, localName, value == null ? null : value.toString());
	}

	@Override
	public IProtocolWriter writeTextOnly(String localName, Value<?> value) {
		return writeTextOnly(null, localName, value);
	}

	@Override
	public IProtocolWriter writeTextOnly(String prefix, String localName, String value) {
		if (value != null) {
			writeElementBegin(prefix, localName);
			writeText(value);
			writeElementEnd();			
		}
		
		return this;
	}

	@Override
	public IProtocolWriter writeTextOnly(String localName, String value) {
		return writeTextOnly(null, localName, value);
	}

	@Override
	public String getDocument() {
		return buffer.toString();
	}

	@Override
	public IProtocolWriter clear() {
		contexts.clear();
		buffer.delete(0, buffer.length());
		
		return this;
	}
	
	@Override
	public String toString() {
		return buffer.toString();
	}

}
