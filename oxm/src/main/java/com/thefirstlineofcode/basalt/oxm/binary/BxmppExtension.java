package com.thefirstlineofcode.basalt.oxm.binary;

import java.util.HashMap;
import java.util.Map;

public class BxmppExtension {
	protected Map<ReplacementBytes, String> replacementBytesToKeywords = new HashMap<ReplacementBytes, String>();
	protected Map<String, ReplacementBytes> keywordsToReplacementBytes = new HashMap<String, ReplacementBytes>();
	
	protected Namespace namespace;
	
	protected BxmppExtension() {}
	
	public BxmppExtension(ReplacementBytes namespaceReplacementBytes, String namespaceKeyword) {
		this(new Namespace(namespaceReplacementBytes, namespaceKeyword));
	}
	
	public BxmppExtension(Namespace namespace) {
		if (namespace == null)
			throw new IllegalArgumentException("Null namespace");
		
		try {
			if (namespace.getReplacementBytes() != null && namespace.getKeyword() != null)
				register(namespace.getReplacementBytes(), namespace.getKeyword());
			this.namespace = namespace;
		} catch (ReduplicateBxmppReplacementException e) {
			// Ignore. It's impossible to execute to here.
		}
		
		
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	public void register(byte replacementByte, String keyword) throws ReduplicateBxmppReplacementException {
		this.register((byte)0xff, replacementByte, keyword);
	}
	
	public synchronized void register(byte firstByteOfReplacementBytes, byte secondByteOfReplacementBytes, String keyword)
			throws ReduplicateBxmppReplacementException {
		register(new ReplacementBytes(firstByteOfReplacementBytes, secondByteOfReplacementBytes), keyword);
	}
	
	public void register(ReplacementBytes replacementBytes, String keyword)
			throws ReduplicateBxmppReplacementException {
		if (replacementBytes == null || keyword == null)
			throw new IllegalArgumentException("Null replacement bytes or null keyword.");
		
		if (namespace != null && ReplacementBytes.isNamespaceReplacementBytes(replacementBytes)) {
			throw new IllegalArgumentException("Try to register multi namespaces in one BXMPP extension.");
		}
		
		ReplacementBytes existedReplacementBytes = keywordsToReplacementBytes.get(keyword);
		String existedKeyword = replacementBytesToKeywords.get(replacementBytes);
		
		if (existedReplacementBytes != null) {
			throw new ReduplicateBxmppReplacementException(existedReplacementBytes, keyword, replacementBytes, keyword);
		}
		
		if (existedKeyword != null) {
			throw new ReduplicateBxmppReplacementException(keywordsToReplacementBytes.get(existedKeyword), existedKeyword, replacementBytes, keyword);
		}
		
		replacementBytesToKeywords.put(replacementBytes, keyword);
		keywordsToReplacementBytes.put(keyword, replacementBytes);
	}
	
	public void unregister(byte replacementByte) {
		this.unregister((byte)0xff, replacementByte);
	}
	
	public synchronized void unregister(byte firstByte, byte secondByte) {
		unregister(new ReplacementBytes(firstByte, secondByte));
	}
	
	public synchronized void unregister(ReplacementBytes replacementBytes) {
		if (ReplacementBytes.isNamespaceReplacementBytes(replacementBytes))
			throw new IllegalArgumentException("Try to unregister namespace replacement bytes.");
		
		String keyword = replacementBytesToKeywords.get(replacementBytes);
		if (keyword == null)
			return;
		
		replacementBytesToKeywords.remove(replacementBytes);
		keywordsToReplacementBytes.remove(keyword);
	}
	
	public String[] getReplacedKeywords() {
		return keywordsToReplacementBytes.keySet().toArray(new String[keywordsToReplacementBytes.size()]);
	}
	
	public ReplacementBytes getReplacementBytes(String keyword) {
		return keywordsToReplacementBytes.get(keyword);
	}
	
	public String getKeyword(ReplacementBytes replacementBytes) {
		return replacementBytesToKeywords.get(replacementBytes);
	}
}
