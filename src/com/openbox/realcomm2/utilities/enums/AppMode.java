package com.openbox.realcomm2.utilities.enums;

public enum AppMode
{
	Initializing("Initializing"),
	Offline("Offline"),
	Online("Online"),
	OutOfRange("Out of Range"),
	Paused("Paused");
	
	private final String displayName;
	
	private AppMode(String displayName)
	{
		this.displayName = displayName;
	}

	public String getDisplayName()
	{
		return displayName;
	}
}
