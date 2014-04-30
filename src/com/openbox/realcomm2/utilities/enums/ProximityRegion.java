package com.openbox.realcomm2.utilities.enums;

import com.openbox.realcomm2.R;

public enum ProximityRegion
{
	Immediate(3.0, R.color.opaque_red),
	Near(8.0, R.color.opaque_orange),
	Far(18.0, R.color.opaque_blue),
	OutOfRange(1000.0, R.color.opaque_light_grey);
	
	private final double proximityLimit;
	private final int colorId;
	
	private ProximityRegion(double limit, int colorId)
	{
		this.proximityLimit = limit;
		this.colorId = colorId;
	}

	public double getProximityLimit()
	{
		return proximityLimit;
	}

	public int getColorId()
	{
		return colorId;
	}
}
