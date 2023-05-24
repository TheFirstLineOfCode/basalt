package com.thefirstlineofcode.basalt.oxm.android;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsers.core.stanza.StanzaParser;
import com.thefirstlineofcode.basalt.oxm.parsing.*;
import com.thefirstlineofcode.basalt.xmpp.core.IError;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.ServiceUnavailable;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Features;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.BadFormat;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.UnsupportedStanzaType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xb.zou
 * @date 2020/2/18
 * @option
 */
public class XmlParsingFactory extends AbstractParsingFactory {
    private static final String LOCAL_NAME_WILDCARD_MATCH = "*";
    private XmlParserFactory parserFactory;

    public XmlParsingFactory() {
        try {
            parserFactory = createParserFactory();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private XmlParserFactory createParserFactory() throws XmlPullParserException {
        return new XmlParserFactory();
    }

    @Override
    protected StanzaError.Kind guessStanzaKind(String message) {
        if (message.startsWith("<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\">"))
            message = message.substring(63);
        if (message.endsWith("</stream:stream>"))
            message = message.substring(0, message.length() - 16);

        int i = 0;

        if (message.charAt(i) != '<')
            return null;
        i++;

        StanzaError.Kind kind;

        if ((message.length() > (i + 2)) && "iq".equals(message.substring(i, i + 2))) {
            kind = StanzaError.Kind.IQ;
            i += 2;
        } else if ((message.length() > (i + 8)) && "presence".equals(message.substring(i, i + 8))) {
            kind = StanzaError.Kind.PRESENCE;
            i += 8;
        } else if ((message.length() > (i + 7)) && "message".equals(message.substring(i, i + 7))) {
            kind = StanzaError.Kind.MESSAGE;
            i += 7;
        } else {
            return null;
        }

        if (message.charAt(i) != ' ')
            return null;

        return kind;
    }

    @Override
    public Object parse(String message) {
        return parse(message, false);
    }

    @Override
    public Object parse(String message, boolean stream) {
        try {
            message = message.trim();

            StringBuilder sb = new StringBuilder();

            boolean isStreamMessage = false;
            if (message.startsWith("<stream:stream")) {
                sb.append(message).append("</stream:stream>");
                isStreamMessage = true;
            } else if (message.equals("</stream:stream>")) {
                sb.append("<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\"><close/></stream:stream>");
                isStreamMessage = true;
            } else {
                sb.append("<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\">");
                sb.append(message);
                sb.append("</stream:stream>");
            }

            Object object = doParse(sb.toString(), stream, isStreamMessage);
            if (object instanceof Stanza) {
                ((Stanza) object).setOriginalMessage(message);
            }

            return object;
        } catch (ProtocolException e) {
            IError error = e.getError();
            if (error instanceof StanzaError) {
                ((StanzaError) error).setOriginalMessage(message);
            }

            throw e;
        }
    }

    private Object doParse(String message, boolean stream, boolean isStreamMessage) {
        XmlPullParser xmlPullParser = null;
        try {
            xmlPullParser = this.createXmlPullParser(message);

            if (!isStreamMessage) {
                xmlPullParser.next();
            }

            Object obj = parseProtocol(xmlPullParser, message);

            if (!isStreamMessage) {
                xmlPullParser.next();
            }

            if (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                throw new BadMessageException("End document expected.");
            }

            return obj;
        } catch (ParsingException e) {
            if (stream) {
                throw new ProtocolException(new BadFormat(), e.getCause());
            }

            throw convertParsingExceptionToProtocolException(e, message);
        } catch (BadMessageException e) {
            throw newBadMessageException(e, stream, message);
        } catch (XmlPullParserException e) {
            throw newBadMessageException(new BadMessageException(String.format("Bad xml format: %d.",
                    e.getColumnNumber()), e), stream, message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T> Object parseProtocol(XmlPullParser xmlPullParser, String message) throws IOException, XmlPullParserException {
        int eventType = xmlPullParser.next();
        if (eventType != XmlPullParser.START_TAG) {
            throw new BadMessageException("No element start tag.");
        }

        return parseProtocol(xmlPullParser, new MessageCarrierParsingContext(message));
    }

    private class MessageCarrierParsingContext implements IParsingContext<Object> {
        private String message;

        public MessageCarrierParsingContext(String message) {
            this.message = message;
        }

        @Override
        public ProtocolChain getProtocolChain() {
            throw new UnsupportedOperationException();
        }

        @Override
        public IParsingPath getParsingPath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getObject() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAttribute(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <K> K getAttribute(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <K> K removeAttribute(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setMessage(String message) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getMessage() {
            return message;
        }

    }

    @Override
    protected boolean isNullParsingContext(IParsingContext<?> parentContext) {
        return parentContext instanceof MessageCarrierParsingContext;
    }

    private <T> Object parseProtocol(XmlPullParser xmlPullParser, IParsingContext<?> parentContext) throws XmlPullParserException {
        Protocol protocol = new Protocol(xmlPullParser.getNamespace(), xmlPullParser.getName());
        ProtocolChain real = createProtocolChain(parentContext, protocol);

        IParser<T> parser = null;
        if (factories.containsKey(real)) {
            parser = getParser(real);
        } else {
            ProtocolChain wildcardMatch = createProtocolChain(parentContext, new Protocol(
                    protocol.getNamespace(), LOCAL_NAME_WILDCARD_MATCH));

            if (factories.containsKey(wildcardMatch)) {
                parser = getParser(wildcardMatch);
            } else {
                // unknown namespace
                try {
                    processUnknownNamespace(xmlPullParser, parentContext, protocol);
                    return null;
                } catch (XmlPullParserException | IOException e) {
                    throw ParsingUtils.newParsingException(xmlPullParser, e.getMessage());
                }
            }
        }

        ParsingContext<T> context = new ParsingContext<>(real, parser.createObject());
        context.setMessage(parentContext.getMessage());

        StanzaError error;
        try {
            error = parseElement(xmlPullParser, parser, context);
        } catch (Exception e) {
            Stanza stanza = getStanza(context);
            if (stanza == null) {
                if (e instanceof XmlPullParserException) {
                    throw (XmlPullParserException) e;
                } else {
                    throw (RuntimeException) e;
                }
            } else {
                throw new ParsingException(e, stanza);
            }
        }

        if (error != null) {
            return error;
        } else {
            Object flawedObject = processFlaws(parentContext, context);

            if (flawedObject != null)
                return flawedObject;

            Object object = context.getObject();
            if (object instanceof Stream) {
                ((Stream) object).setDefaultNamespace(xmlPullParser.getNamespace(null));
            }

            return object;
        }
    }

    private <T> StanzaError parseElement(XmlPullParser xmlPullParser, IParser<T> parser, ParsingContext<T> context) throws IOException, XmlPullParserException {
        IElementParser<T> elementParser = getElementParser(parser, context);

        if (elementParser == null) {
            throw new BadMessageException(String.format("Element %s not allowed be here: %s.",
                    context.getParsingPath(), context));
        }

        List<Attribute> attributes = readAttributes(xmlPullParser);
        StanzaError error = processElementAttributesAndParseError(xmlPullParser, context, elementParser, attributes);
        if (error != null)
            return error;

        StringBuilder text = null;
        while (true) {
            int eventType = xmlPullParser.next();
            if (eventType == XmlPullParser.TEXT) {
                text = ParsingUtils.appendText(xmlPullParser, text);
            } else if (eventType == XmlPullParser.END_TAG) {
                elementParser.processText(context, text == null ? null : Value.create(text.toString()));
                break;
            } else if (eventType == XmlPullParser.START_TAG) {
                String namespace = xmlPullParser.getNamespace();
//                String namespace = xmlPullParser.getNamespaceSafe();
                String localName = xmlPullParser.getName();
                int lastProtocolIndex = context.getProtocolChain().size() - 1;
                String parentNamespace = context.getProtocolChain().get(lastProtocolIndex).getNamespace();
                if (namespace != null && !"".equals(namespace) && !namespace.equals(parentNamespace)) {
                    Object object = parseProtocol(xmlPullParser, context);
                    if (object != null) {
                        parser.processEmbeddedObject(context, new Protocol(namespace, localName), object);
                    }
                } else {
                    context.enter(xmlPullParser.getName());
                    parseElement(xmlPullParser, parser, context);
                    context.exit();
                }
            } else {
                throw ParsingUtils.newParsingException(xmlPullParser, "Unsupported XML event type.");
            }
        }

        return null;
    }



    private <T> StanzaError processElementAttributesAndParseError(XmlPullParser xmlPullParser, ParsingContext<T> context, IElementParser<T> elementParser, List<Attribute> attributes) throws IOException, XmlPullParserException {
        elementParser.processAttributes(context, attributes);
        if (context.removeAttribute(StanzaParser.KEY_ERROR) == StanzaParser.KEY_ERROR) {
            // stanza error is special, so we process it separately
            if (!(context.getObject() instanceof Stanza) && context.getObject() instanceof StanzaError) {
                throw new RuntimeException("Found error. But need a Presence, Message or Iq instance.");
            }

            StanzaErrorParser errorParser = new StanzaErrorParser();
            return errorParser.parse(this, context, xmlPullParser);
        }

        return null;
    }

    private List<Attribute> readAttributes(XmlPullParser xmlPullParser) {
        if (xmlPullParser.getAttributeCount() == 0) {
            return new ArrayList<>();
        }

        List<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String prefix = xmlPullParser.getAttributePrefix(i);
            if ("".equals(prefix)) {
                prefix = null;
            }
            String attributeName = xmlPullParser.getAttributeName(i);
            if ("xmlns".equals(attributeName) || attributeName.startsWith("xmlns:")) {
                continue;
            }

            attributes.add(new Attribute(prefix, attributeName,
                    xmlPullParser.getAttributeValue(i)));
        }

        return attributes;
    }

    private void processUnknownNamespace(XmlPullParser xmlPullParser, IParsingContext<?> parentContext, Protocol protocol) throws IOException, XmlPullParserException {
        if (parentContext == null || (parentContext instanceof MessageCarrierParsingContext)) {
            throw new ProtocolException(new UnsupportedStanzaType(String.format("Unsupported stanza type: %s.", protocol.getLocalName())));
        }

        Protocol topLevelProtocol = parentContext.getProtocolChain().get(0);
        if (!isStanza(topLevelProtocol) && !isFeatures(topLevelProtocol)) {
            throw new ProtocolException(new ServiceUnavailable(String.format("Unsupported protocol: %s.", protocol)));
        }

        int level = 1;
        while (true) {
            int eventType = xmlPullParser.next();
            if (eventType == XmlPullParser.START_TAG) {
                level++;
            } else if (eventType == XmlPullParser.END_TAG) {
                level--;

                if (level == 0) {
                    String localName = xmlPullParser.getName();
                    String namespace = xmlPullParser.getNamespace();

                    if (new Protocol(namespace, localName).equals(protocol)) {
                        List<ProtocolChain> flaws = parentContext.getAttribute(KEY_FLAWS);
                        if (flaws == null)
                            flaws = new ArrayList<>();

                        flaws.add(parentContext.getProtocolChain().next(protocol));

                        parentContext.setAttribute(KEY_FLAWS, flaws);
                        break;
                    }
                }
            } else if (eventType == XmlPullParser.END_DOCUMENT) {
                throw new BadMessageException("Unexpected end of document when processing unkown namespace.");
            } else {
                continue;
            }
        }
    }

    private boolean isFeatures(Protocol protocol) {
        return protocol.equals(Features.PROTOCOL);
    }

    @Override
    protected ProtocolChain createProtocolChain(IParsingContext<?> parsingContext, Protocol protocol) {
        if (parsingContext instanceof MessageCarrierParsingContext) {
            return ProtocolChain.first(protocol);
        } else {
            return parsingContext.getProtocolChain().clone().next(protocol);
        }
    }

    private XmlPullParser createXmlPullParser(String message) throws XmlPullParserException {
        XmlPullParser parser = parserFactory.createParserWrapper(message);

        return parser;
    }

}
