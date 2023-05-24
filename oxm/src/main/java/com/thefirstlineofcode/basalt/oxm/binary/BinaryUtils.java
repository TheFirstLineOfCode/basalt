package com.thefirstlineofcode.basalt.oxm.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class BinaryUtils {

	
	public static String getHexStringFromBytes(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		
		for (byte b : bytes) {
			sb.append(String.format("0x%02x ", b & 0xff));
		}
		
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		return sb.toString();
	}
	
	public static byte[] getBytesFromHexString(String hexString) {
		if (!hexString.startsWith(BinaryConstants.PREFIX_HEX_NUMBER)) {
			throw new IllegalArgumentException("Invalid HEX string: " + hexString);
		}
		
		String[] bytesString = hexString.split(BinaryConstants.PREFIX_HEX_NUMBER);
		byte[] bytes = new byte[bytesString.length - 1];
		for (int i = 1; i < bytesString.length; i++) {
			int num;
			try {
				num = Integer.decode(BinaryConstants.PREFIX_HEX_NUMBER + bytesString[i]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid HEX string: " + hexString);
			}
			
			if (num < 0 || num > 255) {
				throw new IllegalArgumentException("Invalid HEX string: " + hexString);
			}
			
			bytes[i - 1] = (byte)num;
		}
		
		return bytes;
	}
	
	public static String getByteText(byte b) {
		return String.format("%s%d%s",
				BinaryConstants.PREFIX_STRING_OF_BYTE_TYPE,
				b & 0xff,
				BinaryConstants.POSTFIX_STRING_OF_BYTE_TYPE
			);
	}
	
	public static String encodeToBase64(byte[] bytes) {
		return String.format("%s%s%s",
				BinaryConstants.PREFIX_STRING_OF_BASE64_ENCODED,
				Base64.encodeToString(bytes, false),
				BinaryConstants.POSTFIX_STRING_OF_BASE64_ENCODED
			);
	}
	
	public static boolean isBase64Encoded(String content) {
		return content.length() > 4 &&
				content.startsWith(BinaryConstants.PREFIX_STRING_OF_BASE64_ENCODED) &&
				content.endsWith(BinaryConstants.POSTFIX_STRING_OF_BASE64_ENCODED);
	}
	
	public static boolean isByteType(String content) {
		return content.length() > 4 &&
				content.startsWith(BinaryConstants.PREFIX_STRING_OF_BYTE_TYPE) &&
				content.endsWith(BinaryConstants.POSTFIX_STRING_OF_BYTE_TYPE);
	}
	
	public static boolean isBytesType(byte[] bytes) {
		return bytes.length >= 2 &&
				bytes[0] == BinaryConstants.FLAG_BYTES_TYPE;
	}
	
	public static boolean isByteType(byte[] bytes) {
		return (bytes.length == 2 || bytes.length == 3) &&
				bytes[0] == BinaryConstants.FLAG_BYTE_TYPE;
	}
	
	public static byte[] escape(byte[] bytes) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == BinaryConstants.FLAG_DOC_BEGINNING_END ||
						bytes[i] == BinaryConstants.FLAG_UNIT_SPLITTER ||
						bytes[i] == BinaryConstants.FLAG_NOREPLACE ||
						bytes[i] == BinaryConstants.FLAG_ESCAPE ||
						bytes[i] == BinaryConstants.FLAG_BYTES_TYPE ||
						bytes[i] == BinaryConstants.FLAG_BYTE_TYPE) {
					output.write(BinaryConstants.FLAG_ESCAPE);
					output.write(bytes[i]);
				} else {
					output.write(bytes[i]);
				}
			}
			
			bytes = output.toByteArray();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		if (bytes.length == 1) {
			return new byte[] {BinaryConstants.FLAG_NOREPLACE, bytes[0]};
		}
		
		return bytes;
	}
	
	public static byte[] getBytesWithBytesTypeFlag(byte[] bytes) {		
		if (bytes.length == 2 && bytes[0] == BinaryConstants.FLAG_NOREPLACE) {
			bytes[0] = BinaryConstants.FLAG_BYTES_TYPE;
			return bytes;
		}
		
		byte[] withBytesTypeFlag = new byte[bytes.length + 1];
		withBytesTypeFlag[0] = BinaryConstants.FLAG_BYTES_TYPE;
		for (int i = 0; i < bytes.length; i++)
			withBytesTypeFlag[i + 1] = bytes[i];
		
		return withBytesTypeFlag;
	}
	
	public static byte[] getBytesWithByteTypeFlag(byte[] bytes) {
		if (bytes.length == 2 && bytes[0] == BinaryConstants.FLAG_NOREPLACE) {
			bytes[0] = BinaryConstants.FLAG_BYTE_TYPE;
			return bytes;
		}
		
		if (bytes.length == 2 && bytes[0] == BinaryConstants.FLAG_ESCAPE) {
			return new byte[] {BinaryConstants.FLAG_BYTE_TYPE, bytes[0], bytes[1]};
		}
		
		if (bytes.length != 1)
			throw new IllegalArgumentException("Not a byte.");
		
		return new byte[] {BinaryConstants.FLAG_BYTE_TYPE, bytes[0]};
	}
	
	public static byte[] unescape(byte[] bytes) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == BinaryConstants.FLAG_ESCAPE && i < (bytes.length - 1) && isEscapedByte(bytes[i + 1])) {
					continue;
				}
				
				output.write(bytes[i]);
			}
			
			bytes = output.toByteArray();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					throw new RuntimeException("????", e);
				}
		}
		
		if (bytes.length == 1)
			return bytes;
		
		if (bytes[0] == BinaryConstants.FLAG_BYTES_TYPE)
			return Arrays.copyOfRange(bytes, 1, bytes.length);
		
		if (bytes.length == 2 && bytes[0] == BinaryConstants.FLAG_BYTE_TYPE)
			return new byte[] {bytes[1]};
		
		if (bytes.length == 2 && bytes[0] == BinaryConstants.FLAG_NOREPLACE) {
			return new byte[] {bytes[1]};
		}
		
		return bytes;
	}
	
	public static boolean isEscapedByte(byte b) {
		return (b & 0xff) >= 0xfa && (b & 0xff) <= 0xff;
	}
	
	public static byte[] decodeFromBase64(String content) {
		if (!isBase64Encoded(content))
			throw new IllegalArgumentException(String.format("Not a Base64 encoded content. Content: %s.", content));
		
		String encodedPart = content.substring(3, content.length() - 1);
		return Base64.decode(encodedPart);
	}
	
	public static byte getByte(String content) {
		if (!isByteType(content))
			throw new IllegalArgumentException(String.format("Not a byte type content. Content: %s.", content));
		
		String byteValue = content.substring(3, content.length() - 1);
		return Byte.parseByte(byteValue);
	}
	
	public static boolean isLegalBxmppMessage(byte[] message) {
		return message.length >= 5 && message[0] == BinaryConstants.FLAG_DOC_BEGINNING_END
				|| message[message.length - 1] == BinaryConstants.FLAG_DOC_BEGINNING_END;
	}
}
