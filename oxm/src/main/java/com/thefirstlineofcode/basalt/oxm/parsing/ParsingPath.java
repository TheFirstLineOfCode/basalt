package com.thefirstlineofcode.basalt.oxm.parsing;

import java.util.Stack;
import java.util.StringTokenizer;

public final class ParsingPath implements IParsingPath {
	private Stack<String> paths;
	
	public ParsingPath() {
		paths = new Stack<>();
		paths.push("/");
	}
	
	public void enter(String path) {
		paths.push(path);
	}
	
	public void exit() {
		if (paths.size() > 1)
			paths.pop();
	}
	
	@Override
	public int hashCode() {
		if (paths.isEmpty())
			return 0;
		
		int hash = 7;
		for (String path : paths) {
			hash += 31 * hash + path.hashCode();
		}
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj instanceof ParsingPath) {
			ParsingPath other = (ParsingPath)obj;
			
			if (paths.size() != other.paths.size())
				return false;
			
			for (int i = 0; i < paths.size(); i++) {
				if (!paths.get(i).equals(other.paths.get(i)))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paths.size(); i++) {
			if (sb.length() != 0 && sb.charAt(sb.length() - 1) != '/') {
				sb.append('/');
			}
			
			sb.append(paths.get(i));
		}
		
		return sb.toString();
	}

	@Override
	public boolean match(String parsingPathPattern) {
		if ("/".equals(parsingPathPattern)) {
			return paths.size() == 1;
		}
		
		StringTokenizer st;
		if (parsingPathPattern.startsWith("/")) {
			st = new StringTokenizer(parsingPathPattern.substring(1), "/");
		} else {
			st = new StringTokenizer(parsingPathPattern, "/");
		}
		
		ParsingPath implSpecific = new ParsingPath();
		while (st.hasMoreTokens()) {
			String path = st.nextToken();
			implSpecific.enter(path);
		}
		
		return this.equals(implSpecific);
	}
	
	public Stack<String> getPaths() {
		return paths;
	}
}
