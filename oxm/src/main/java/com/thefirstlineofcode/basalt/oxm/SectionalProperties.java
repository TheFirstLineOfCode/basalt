package com.thefirstlineofcode.basalt.oxm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SectionalProperties {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private Map<String, Properties> sections;
	
	public SectionalProperties() {
		sections = new HashMap<>();
	}
	
	public void setProperties(String name, Properties properties) {
		sections.put(name, properties);
	}
	
	public Set<String> getSectionNames() {
		return sections.keySet();
	}
	
	public Properties getSection(String name) {
		return sections.get(name);
	}
	
	public void load(InputStream inputStream) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
			boolean lineContinuation = false;
			Section section = null;
			
			int currentLines = 0;
			while ((line = in.readLine()) != null) {
				currentLines++;
				line = line.trim();
				
				if (line.isEmpty()) {
					// ignore
					continue;
				}
				
				if (isSectionName(line)) {
					if (section != null) {
						sections.put(section.name, loadProperties(section.content.toString()));
					}
					
					section = new Section();
					section.name = getSectionName(line);
					if (sections.containsKey(section.name))
						throw new IllegalArgumentException(String.format("Reduplicate section: %s.", section.name));
					
					lineContinuation = false;
				} else if (isComment(line)) {
					// ignore
				} else if (isSectionContent(line) || lineContinuation) {
					if (section == null) {
						throw new IllegalArgumentException(String.format("Illegal sectional properties file. Null section name. Illegal line is [line number: %d, line content: '%s'].", currentLines, line));
					}
					
					if (section.content.length() == 0) {
						section.content.append(line);
					} else {
						section.content.append(LINE_SEPARATOR).append(line);
					}
					
					if (isLineContinuation(line))
						lineContinuation = true;
					else
						lineContinuation = false;
				} else {
					throw new IllegalArgumentException(String.format("Illegal sectional properties file. Illegal line is [line number: %d, line content: '%s'].", currentLines, line));
				}
			}
			
			if (section != null) {
				sections.put(section.name, loadProperties(section.content.toString()));
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isLineContinuation(String line) {
		return line.charAt(line.length() - 1) == '\\';
	}
	
	private Properties loadProperties(String content) {
		Properties properties = new Properties();
		try {
			properties.load(new  ByteArrayInputStream(content.getBytes()));
		} catch (IOException e) {
			throw new RuntimeException(String.format("Can't load properties. Section content is %s.", content), e);
		}
		
		return properties;
	}

	private boolean isComment(String line) {
		return line.startsWith("#");
	}
	
	private boolean isSectionContent(String line) {
		if (line.length() < 3)
			return false;
		
		int equalMarkIndex = line.indexOf('=');
		
		if (equalMarkIndex == -1 || equalMarkIndex == 0 || equalMarkIndex == line.length() - 1)
			return false;
		
		return true;
	}
	
	private String getSectionName(String line) {
		return line.substring(1, line.length() - 1);
	}
	
	private class Section {
		public String name;
		public StringBuilder content = new StringBuilder();
	}
	
	private boolean isSectionName(String line) {
		return line.startsWith("[") && line.endsWith("]") && line.length() > 3;
	}
	
	public void save(OutputStream outputStream) {
		// TODO to implement save operation
	}
	
}
