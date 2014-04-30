package com.openbox.realcomm2.database.models;

import java.util.LinkedList;

import com.openbox.realcomm2.database.objects.Booth;


public class BoothDistanceModel implements Comparable<BoothDistanceModel>
{
	public static final int WINDOW_LENGTH = 5;
	private static final float DEFAULT_RSSI = -120.0f; // Might need to tune this
	private static final float DEFAULT_ACCURACY = 50.0f; // Might need to tune this

	/**********************************************************************************************
	 * Fields
	 **********************************************************************************************/
	private LinkedList<Float> rssiHistory = new LinkedList<Float>();
	private LinkedList<Float> accuracyHistory = new LinkedList<Float>();

	private int boothId;
	private String UUID;
	private int major;
	private int minor;

	private float averageRssi;
	private float averageAccuracy;

	/**********************************************************************************************
	 * Getters
	 **********************************************************************************************/
	public int getBoothId()
	{
		return boothId;
	}

	public String getUUID()
	{
		return UUID;
	}

	public int getMajor()
	{
		return major;
	}

	public int getMinor()
	{
		return minor;
	}

	public float getAverageRSSI()
	{
		return averageRssi;
	}

	public float getAverageAccuracy()
	{
		return averageAccuracy;
	}

	/**********************************************************************************************
	 * Constructor
	 **********************************************************************************************/
	public BoothDistanceModel(Booth booth)
	{
		this.boothId = booth.getBoothId();
		this.UUID = booth.getUUID();
		this.major = booth.getMajor();
		this.minor = booth.getMinor();
	}

	/**********************************************************************************************
	 * Public Methods
	 **********************************************************************************************/
	public void addRssi(Float rssi)
	{
		if (this.rssiHistory.size() >= WINDOW_LENGTH)
		{
			this.rssiHistory.remove();
		}

		this.rssiHistory.add(rssi != null ? rssi : DEFAULT_RSSI);

		calculateAverageRssi();
	}
	
	public void addAccuracy(Float accuracy)
	{
		if (this.accuracyHistory.size() >= WINDOW_LENGTH)
		{
			this.accuracyHistory.remove();
		}
		
		this.accuracyHistory.add(accuracy != null ? accuracy : DEFAULT_ACCURACY);
		
		calculateAverageAccuracy();
	}

	/**********************************************************************************************
	 * Private methods
	 **********************************************************************************************/
	private void calculateAverageRssi()
	{
		if (this.rssiHistory.size() == 0)
		{
			this.averageRssi = DEFAULT_RSSI;
			return;
		}
		
		this.averageRssi = 0.0f;
		for (int i = 0; i < this.rssiHistory.size(); i++)
		{
			this.averageRssi += this.rssiHistory.get(i);
		}

		this.averageRssi = this.averageRssi / (float) this.rssiHistory.size();
	}
	
	private void calculateAverageAccuracy()
	{
		if (this.accuracyHistory.size() == 0)
		{
			this.averageAccuracy = DEFAULT_ACCURACY;
			return;
		}
		
		this.averageAccuracy = 0.0f;
		for (int i = 0; i < this.accuracyHistory.size(); i++)
		{
			this.averageAccuracy += this.accuracyHistory.get(i);
		}

		this.averageAccuracy = this.averageAccuracy / (float) this.accuracyHistory.size();
	}

	/**********************************************************************************************
	 * Compare and equality overrides
	 **********************************************************************************************/
	@Override
	public int compareTo(BoothDistanceModel another)
	{
		final int CLOSER = -1;
		final int EQUAL = 0;
		final int FARTHER = 1;

		// Closer to 0 is closer, -120 is farthest
//		if (this.getAverageRSSI() > another.getAverageRSSI())
//			return CLOSER;
//		if (this.getAverageRSSI() == another.getAverageRSSI())
//			return EQUAL;
//		else
//			return FARTHER;
		
		// TODO: Play around with which gives better results (or a mix thereof)
		if (this.getAverageAccuracy() < another.getAverageAccuracy())
			return CLOSER;
		if (this.getAverageAccuracy() == another.getAverageAccuracy())
			return EQUAL;
		else
			return FARTHER;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UUID == null) ? 0 : UUID.hashCode());
		result = prime * result + boothId;
		result = prime * result + major;
		result = prime * result + minor;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BoothDistanceModel other = (BoothDistanceModel) obj;
		if (UUID == null)
		{
			if (other.UUID != null) return false;
		}
		else if (!UUID.equals(other.UUID)) return false;
		if (boothId != other.boothId) return false;
		if (major != other.major) return false;
		if (minor != other.minor) return false;
		return true;
	}
}