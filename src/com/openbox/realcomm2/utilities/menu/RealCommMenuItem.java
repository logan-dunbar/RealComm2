package com.openbox.realcomm2.utilities.menu;

public class RealCommMenuItem
{
	private String displayName;
	private Class<?> classToStart;

	public String getDisplayName()
	{
		return this.displayName;
	}

	public Class<?> getClassToStart()
	{
		return this.classToStart;
	}
	
	public RealCommMenuItem(String displayName, Class<?> classToStart)
	{
		this.displayName = displayName;
		this.classToStart = classToStart;
	}
}
