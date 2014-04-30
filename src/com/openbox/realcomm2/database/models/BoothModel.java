package com.openbox.realcomm2.database.models;

import java.util.LinkedList;

import android.content.res.Resources;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.database.objects.Booth;
import com.openbox.realcomm2.utilities.enums.ProximityRegion;
import com.radiusnetworks.ibeacon.IBeacon;

public class BoothModel
{
	private static final int WINDOW_LENGTH = 5;
	private static final int DEFAULT_RSSI = -120;
	private static final int DEFAULT_TX_POWER = -76;

	private int boothId;
	private int boothNumber;
	private String uuid;
	private int major;
	private int minor;

	private Double accuracy = null;
	private Double runningAverageRssi = null;

	private LinkedList<Integer> rssiHistory = new LinkedList<Integer>();

	public BoothModel(Booth booth)
	{
		if (booth != null)
		{
			this.boothId = booth.getBoothId();
			this.boothNumber = booth.getBoothNumber();
			this.uuid = booth.getUUID();
			this.major = booth.getMajor();
			this.minor = booth.getMinor();
		}
	}

	public Double getAccuracy()
	{
		if (accuracy == null)
		{
			updateDistanceWithDefault();
		}

		return accuracy;
	}
	
	public void resetDistance()
	{
		this.rssiHistory.clear();
		updateDistanceWithDefault();
	}

	public void updateDistanceWithBeacon(IBeacon beacon)
	{
		this.rssiHistory.add(beacon.getRssi());
		updateDistance(beacon.getTxPower());

	}

	public void updateDistanceWithDefault()
	{
		this.rssiHistory.add(DEFAULT_RSSI);
		updateDistance(DEFAULT_TX_POWER);
	}

	private void updateDistance(int txPower)
	{
		if (this.rssiHistory.size() >= WINDOW_LENGTH)
		{
			this.rssiHistory.poll();
		}

		calculateRunningAverageRssi();
		calculateAccuracy(txPower);
	}

	private void calculateRunningAverageRssi()
	{
		if (this.rssiHistory.size() == 0)
		{
			this.runningAverageRssi = (double) DEFAULT_RSSI;
		}
		
		this.runningAverageRssi = 0.0;
		for (int i = 0; i < this.rssiHistory.size(); i++)
		{
			this.runningAverageRssi += this.rssiHistory.get(i);
		}

		this.runningAverageRssi = this.runningAverageRssi / this.rssiHistory.size();
	}

	private void calculateAccuracy(int txPower)
	{
		double ratio = (this.runningAverageRssi * 1.0) / txPower;
		if (ratio < 1.0)
		{
			this.accuracy = Math.pow(ratio, 10);
		}
		else
		{
			this.accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
		}
	}

	public int getColor(Resources resources)
	{
		return resources.getColor(getProximityRegion().getColorId());
	}
	
	public ProximityRegion getProximityRegion()
	{
		if (this.getAccuracy() < ProximityRegion.Immediate.getProximityLimit())
		{
			return ProximityRegion.Immediate;
		}
		else if (this.getAccuracy() < ProximityRegion.Near.getProximityLimit())
		{
			return ProximityRegion.Near;
		}
		else if (this.getAccuracy() < ProximityRegion.Far.getProximityLimit())
		{
			return ProximityRegion.Far;
		}
		else
		{
			return ProximityRegion.OutOfRange;
		}
	}

	public int getBoothId()
	{
		return boothId;
	}

	public void setBoothId(int boothId)
	{
		this.boothId = boothId;
	}

	public int getBoothNumber()
	{
		return boothNumber;
	}

	public void setBoothNumber(int boothNumber)
	{
		this.boothNumber = boothNumber;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public int getMajor()
	{
		return major;
	}

	public void setMajor(int major)
	{
		this.major = major;
	}

	public int getMinor()
	{
		return minor;
	}

	public void setMinor(int minor)
	{
		this.minor = minor;
	}
}
