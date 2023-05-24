package com.thefirstlineofcode.basalt.xmpp.core.stream;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class Features {
	public static final Protocol PROTOCOL = new Protocol("http://etherx.jabber.org/streams", "features");
	
	private List<Feature> features;

	public List<Feature> getFeatures() {
		if (features == null) {
			features = new ArrayList<>();
		}
		
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
}
