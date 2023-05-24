package com.thefirstlineofcode.basalt.oxm.parsing;

import java.util.List;

import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Feature;

public class FlawedProtocolFeature extends FlawedProtocolObject implements Feature {
	public FlawedProtocolFeature() {
		super();
	}
	
	public FlawedProtocolFeature(List<ProtocolChain> flaws) {
		super(flaws);
	}
}
