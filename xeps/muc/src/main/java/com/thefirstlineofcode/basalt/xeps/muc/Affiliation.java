package com.thefirstlineofcode.basalt.xeps.muc;

public enum Affiliation {
	OWNER,
	ADMIN,
	MEMBER,
	OUTCAST,
	NONE;
	
	public int getRank() {
		if (this == OWNER) {
			return 3;
		} else if (this == ADMIN) {
			return 2;
		} else if (this == MEMBER) {
			return 1;
		} else if (this == OUTCAST) {
			return -1;
		} else {
			return 0;
		}
	}
}
