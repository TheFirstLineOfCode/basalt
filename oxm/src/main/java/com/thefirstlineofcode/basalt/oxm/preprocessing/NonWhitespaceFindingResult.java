package com.thefirstlineofcode.basalt.oxm.preprocessing;

public class NonWhitespaceFindingResult {
	public boolean found;
	public int prefixWhitespaceLength;
	
	public NonWhitespaceFindingResult() {
		this(false, 0);
	}
	
	public NonWhitespaceFindingResult(boolean found, int prefixWhitespaceLength) {
		this.found = found;
		this.prefixWhitespaceLength = prefixWhitespaceLength;
	}
}
