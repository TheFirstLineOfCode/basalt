package com.thefirstlineofcode.basalt.oxm.binary;

public class ReplacementBytes {
	private byte first;
	private byte second;
	private String provider;
	
	public ReplacementBytes(byte second) {
		this((byte)0xff, second);
	}
	
	public ReplacementBytes(byte first, byte second) {
		this.first = first;
		this.second = second;
		
		if (!isFirstByteOfSingleByteReplacementBytes(first) &&
				!isFirstByteOfDoubleBytesNamespaceReplacementBytes(first)) {
			throw new IllegalArgumentException("Invalid first byte of namespace replacement bytes.");
		}
	}
	
	public void setProvider(String provider) {
		if (provider != null) {
			this.provider = provider.intern();
		} else {
			this.provider = null;
		}
	}
	
	public String getProvider() {
		return provider;
	}
	
	public byte getFirst() {
		return first;
	}
	
	public byte getSecond() {
		return second;
	}
	
	@Override
	public int hashCode() {
		if (first == (byte)0xff) {
			return second & 0xff;
		} else {
			return 31 * (first & 0xff) + (second & 0xff);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReplacementBytes) {
			ReplacementBytes other = (ReplacementBytes)obj;
			
			return other.first == this.first && other.second == this.second;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		if (first == (byte)0xff) {
			return String.format("%s[0xff, %s]", ReplacementBytes.class.getSimpleName(),
					BinaryUtils.getHexStringFromBytes(new byte[] {second}));
		} else {
			return String.format("%s[%s, %s]", ReplacementBytes.class.getSimpleName(),
					BinaryUtils.getHexStringFromBytes(new byte[] {first}),
					BinaryUtils.getHexStringFromBytes(new byte[] {second}));
		}
	}
	
	public static ReplacementBytes parse(String rbString) {
		byte[] bytes = BinaryUtils.getBytesFromHexString(rbString);
		
		if (bytes.length == 1) {
			bytes = new byte[] {(byte)0xff, bytes[0]};
		}
		
		if (bytes.length != 2) {
			throw new ReplacementBytesFormatException("Invalid replacment bytes string: " + rbString);
		}
		
		if (!isFirstByteOfSingleByteReplacementBytes(bytes[0]) &&
				!isFirstByteOfDoubleBytesNamespaceReplacementBytes(bytes[0])) {
			throw new ReplacementBytesFormatException("Invalid replacment bytes string: " + rbString);
		}
			
		return new ReplacementBytes(bytes[0], bytes[1]);
	}

	private static boolean isFirstByteOfSingleByteReplacementBytes(byte first) {
		return (first & 0xff) == 0xff;
	}
	
	public byte[] toBytes() {
		if (first == (byte)0xff)
			return new byte[] {second};
		
		return new byte[] {first, second};
	}
	
	public static boolean isNamespaceReplacementBytes(ReplacementBytes replacementBytes) {
		return isFirstByteOfDoubleBytesNamespaceReplacementBytes(replacementBytes.getFirst());
	}
	
	public static boolean isFirstByteOfDoubleBytesNamespaceReplacementBytes(byte first) {
		return (first & 0xff) != 0xff && (first & 0xff) >= 0xf0 && (first & 0xff) < 0xfa;
	}
}
