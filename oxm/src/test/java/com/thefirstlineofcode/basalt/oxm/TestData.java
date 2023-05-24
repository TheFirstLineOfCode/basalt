package com.thefirstlineofcode.basalt.oxm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.thefirstlineofcode.basalt.oxm.binary.BinaryUtils;
import com.thefirstlineofcode.basalt.xmpp.Constants;


public class TestData {
	private static final Map<String, Properties> propertiesCache = new HashMap<>();
	
	public static synchronized String getData(Class<?> clazz, String name) {
		String dataFileName = "/" + clazz.getName();
		
		Properties properties = propertiesCache.get(dataFileName);
		if (properties == null) {
			Reader reader = null;
			properties = new Properties();
			try {
				reader = new BufferedReader(new InputStreamReader(clazz.getResource(dataFileName).openStream(),
						Constants.DEFAULT_CHARSET));
				properties.load(reader);
				
				propertiesCache.put(dataFileName, properties);
			} catch (Exception e) {
				throw new RuntimeException("can't load data file", e);
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						// ignore
					}
			}
		}
		
		String data = properties.getProperty(name);
		while (true) {
			int referenceStart = data.indexOf("${");
			if (referenceStart == -1)
				break;
			
			int referenceEnd = data.indexOf("}", referenceStart + 2);
			
			if (referenceEnd == -1)
				break;
			
			String referenceName = data.substring(referenceStart + 2, referenceEnd);
			
			String reference = (String)properties.get(referenceName);
			
			if (reference == null) {
				throw new RuntimeException(String.format("illegal reference name %s in data file %s",
						reference, dataFileName));
			}
			
			data = data.substring(0, referenceStart) + reference + data.substring(referenceEnd + 1);
		}
		
		return data;
	}
	
	public static byte[] toBinaryBytes(String binaryMessageString) {
		return BinaryUtils.getBytesFromHexString(binaryMessageString);
	}
}
