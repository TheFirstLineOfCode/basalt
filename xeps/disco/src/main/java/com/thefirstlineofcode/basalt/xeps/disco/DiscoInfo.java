package com.thefirstlineofcode.basalt.xeps.disco;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.xeps.xdata.XData;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="http://jabber.org/protocol/disco#info", localName="query")
public class DiscoInfo {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/disco#info", "query");
	
	private String node;
	@Array(Identity.class)
	private List<Identity> identities;
	@Array(Feature.class)
	private List<Feature> features;
	
	private XData xData;
	
	public DiscoInfo() {}
	
	public DiscoInfo(String node) {
		this.node = node;
	}
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}
	
	public List<Identity> getIdentities() {
		if (identities == null) {
			identities = new ArrayList<>();
		}
		return identities;
	}
	
	public void setIdentities(List<Identity> identities) {
		this.identities = identities;
	}
	
	public List<Feature> getFeatures() {
		if (features == null) {
			features = new ArrayList<>();
		}
		return features;
	}
	
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public XData getXData() {
		return xData;
	}

	public void setXData(XData xData) {
		this.xData = xData;
	}
	
}
