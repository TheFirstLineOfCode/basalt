package com.thefirstlineofcode.basalt.oxm.binary;

public class Namespace {
	private ReplacementBytes replacementBytes;
	private String keyword;
	
	public Namespace(ReplacementBytes replacementBytes, String keyword) {
		this.replacementBytes = replacementBytes;
		this.keyword = keyword;
		
		if (replacementBytes != null && !ReplacementBytes.isFirstByteOfDoubleBytesNamespaceReplacementBytes(replacementBytes.getFirst()))
			throw new IllegalArgumentException("Illegal BXMPP extension protocol namespace replacement bytes. First byte value of namespace replacement bytes must be in range of 0xf0~0xf9.");
	}
	
	public ReplacementBytes getReplacementBytes() {
		return replacementBytes;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		
		if (replacementBytes != null)
			hash += 31 * replacementBytes.hashCode();
		if (keyword == null)
			hash += keyword.hashCode();
		
		return hash;
	}
	
	@Override
	public String toString() {
		return String.format("Namespace[%s, %s]", replacementBytes.toString(), keyword);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Namespace))
			return false;
		
		Namespace other = (Namespace)obj;
		
		return isObjectSame(this.replacementBytes, other.replacementBytes) && isObjectSame(this.keyword, other.keyword);
	}

	private boolean isObjectSame(Object obj1, Object obj2) {
		if (obj1 == null) {
			return obj2 == null;
		} else {
			return obj1.equals(obj2);
		}
	}
}
