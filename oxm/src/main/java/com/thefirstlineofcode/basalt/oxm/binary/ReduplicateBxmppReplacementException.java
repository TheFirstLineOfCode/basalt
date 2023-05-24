package com.thefirstlineofcode.basalt.oxm.binary;

public class ReduplicateBxmppReplacementException extends Exception {

	private static final long serialVersionUID = -7008889550910030835L;
	
	private ReplacementBytes oldReplacementByte;
	private ReplacementBytes newReplacementByte;
	private String oldKeyword;
	private String newKeyword;
	
	public ReduplicateBxmppReplacementException(ReplacementBytes replacementByte1, String keyword1, ReplacementBytes replacementByte2, String keyword2) {
		this.oldReplacementByte = replacementByte1;
		this.oldKeyword = keyword1;
		this.newReplacementByte = replacementByte2;
		this.newKeyword = keyword2;
	}
	
	public ReplacementBytes getOldReplacementByte() {
		return oldReplacementByte;
	}
	
	public ReplacementBytes getNewReplacementByte() {
		return newReplacementByte;
	}
	
	public String getOldKeyword() {
		return oldKeyword;
	}
	
	public String getNewKeyword() {
		return newKeyword;
	}
	
	@Override
	public String toString() {
		return String.format("BXMPP Replacement conflict: %s:%s->%s and %s:%s->%s", oldReplacementByte.getProvider(), oldReplacementByte, oldKeyword, newReplacementByte.getProvider(), newReplacementByte, newKeyword);
	}
	
}
