package com.genesyslab.machi.domain.type;

public enum StandupEventType {
	
	PAST("Past Days"),
	NEXT("Next Days"),
	IMPEDIMENT("Impediments");

	private String message;

	public String getMessage() {
		return this.message;
	}

	private StandupEventType(String message) {
		this.message = message;
	}

}