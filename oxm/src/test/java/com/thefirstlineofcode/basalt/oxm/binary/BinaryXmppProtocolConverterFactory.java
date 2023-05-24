package com.thefirstlineofcode.basalt.oxm.binary;

public class BinaryXmppProtocolConverterFactory {
	private static final Namespace COMMAND_EXTENSION_NAMESPACE = new Namespace(new ReplacementBytes((byte)0xf9, (byte)0x00),
			"http://jabber.org/protocol/commands");
	private static final Namespace XDATA_EXTENSION_NAMESPACE = new Namespace(new ReplacementBytes((byte)0xf9, (byte)0x01),
			"jabber:x:data");
	private static final Namespace LORA_ADDRESS_EXTENSION_NAMESPACE = new Namespace(new ReplacementBytes((byte)0xf8, (byte)0x00),
			"urn:leps:basalt:test:lora");
	
	private static final String[] DEFAULT_KEYWORDS = new String[] {
			"iq", "from", "to", "type", "set", "xml:lang", "en", "id",
			"message", "chat", "subject", "zh", "body",
			"jabber:client", "xmlns:stream", "http://etherx.jabber.org/streams", "version", "1.0"
	};
	
	private static final String[] COMMAND_EXTENSION_KEYWORDS = new String[] {
			"command", "node", "create", "sessionid",
			
	};
	
	private static final String[] XDATA_EXTENSION_KEYWORDS = new String[] {
			"x", "type", "submit", "field", "hidden", "var", "form_type", "value",
			"text-single", "text-multi", "boolean", "text-private", "list-multi", "list-single", "jid-multi"
	};
	
	private static final String[] LORA_ADDRESS_EXTENSION_KEYWORDS = new String[] {
			"lora-address", "address", "channel"
	};
	
	private static IBinaryXmppProtocolConverter instance;
	
	public static IBinaryXmppProtocolConverter getInstance() {
		if (instance == null) {
			try {
				instance = createInstance();
			} catch (ReduplicateBxmppReplacementException e) {
				// It isn't possible that the program runs to here.
				throw new RuntimeException("Can't create binary XMPP protocol converter.");
			}
		}
		
		return instance;
	}

	private static IBinaryXmppProtocolConverter createInstance() throws ReduplicateBxmppReplacementException {
		BxmppExtension defaultExtension = new DefaultBxmppExtension();
		for (int i = 0; i < DEFAULT_KEYWORDS.length; i++) {
			defaultExtension.register((byte)i, DEFAULT_KEYWORDS[i]);
		}
		
		BxmppExtension commandExtension = new BxmppExtension(COMMAND_EXTENSION_NAMESPACE);
		for (int i = 0; i < COMMAND_EXTENSION_KEYWORDS.length; i++) {
			commandExtension.register((byte)i, COMMAND_EXTENSION_KEYWORDS[i]);
		}
		
		BxmppExtension xdataExtension = new BxmppExtension(XDATA_EXTENSION_NAMESPACE);
		for (int i = 0; i <  XDATA_EXTENSION_KEYWORDS.length; i++) {
			xdataExtension.register((byte)i, XDATA_EXTENSION_KEYWORDS[i]);
		}
		
		BxmppExtension loraAddressExtension = new BxmppExtension(LORA_ADDRESS_EXTENSION_NAMESPACE);
		for (int i = 0; i <  LORA_ADDRESS_EXTENSION_KEYWORDS.length; i++) {
			loraAddressExtension.register((byte)i, LORA_ADDRESS_EXTENSION_KEYWORDS[i]);
		}
		
		BinaryXmppProtocolConverter protocolConverter = new BinaryXmppProtocolConverter();
		protocolConverter.register(defaultExtension);
		protocolConverter.register(commandExtension);
		protocolConverter.register(xdataExtension);
		protocolConverter.register(loraAddressExtension);
		
		return protocolConverter;
	}
}
