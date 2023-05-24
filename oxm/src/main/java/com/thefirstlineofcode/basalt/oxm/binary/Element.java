package com.thefirstlineofcode.basalt.oxm.binary;

import java.util.ArrayList;
import java.util.List;

public class Element {
	public String namespace;
	public String localName;
	public List<NameAndValue> attributes;
	public List<Element> children;
	public String text;
	
	public int endPosition;
	
    public Element() {
    	attributes = new ArrayList<NameAndValue>();
    	children = new ArrayList<Element>();
    }
    
    public static class NameAndValue {
        public String name;
        public String value;

        public NameAndValue(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + value.hashCode();
        }

        @Override
        public String toString() {
            return name + "=" + value;
        }
    }

    public static class NextPart {
        public ReplacementBytes replacementBytes;
        public String text;
        public int endPostion;
        
        public NextPart(ReplacementBytes replacementBytes, String text, int endPosition) {
            this.replacementBytes  = replacementBytes;
            this.text = text;
            this.endPostion = endPosition;
        }
    }
}
