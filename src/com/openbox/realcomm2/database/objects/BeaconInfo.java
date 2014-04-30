package com.openbox.realcomm2.database.objects;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.radiusnetworks.ibeacon.IBeacon;

@DatabaseTable(tableName = "BeaconInfo")
public class BeaconInfo implements Serializable
{
	private static final long serialVersionUID = 6637276678914198411L;

	public static final String BEACON_INFO_ID_COLUMN_NAME = "BeaconInfoId";
	public static final String UUID_COLUMN_NAME = "UUID";
	public static final String MAJOR_COLUMN_NAME = "Major";
	public static final String MINOR_COLUMN_NAME  = "Minor";
	public static final String NAME_COLUMN_NAME  = "Name";
	public static final String MEASURED_POWER_COLUMN_NAME  = "MeasuredPower";
	public static final String RSSI_COLUMN_NAME  = "RSSI";
	public static final String ACCURACY_COLUMN_NAME = "Accuracy";
	public static final String TAG_NAME_COLUMN_NAME = "TagName";
	public static final String INSERT_DATE_COLUMN_NAME = "InsertDate";
	
	@DatabaseField(generatedId = true, columnName = BEACON_INFO_ID_COLUMN_NAME)
	@SerializedName(BEACON_INFO_ID_COLUMN_NAME)
	private int beaconInfoId;
	
	@DatabaseField(columnName = UUID_COLUMN_NAME)
	@SerializedName(UUID_COLUMN_NAME)
	private String uuid;
	
	@DatabaseField(columnName = MAJOR_COLUMN_NAME)
	@SerializedName(MAJOR_COLUMN_NAME)
	private int major;
	
	@DatabaseField(columnName = MINOR_COLUMN_NAME)
	@SerializedName(MINOR_COLUMN_NAME)
	private int minor;
	
	@DatabaseField(columnName = NAME_COLUMN_NAME)
	@SerializedName(NAME_COLUMN_NAME)
	private String address;
	
	@DatabaseField(columnName = MEASURED_POWER_COLUMN_NAME)
	@SerializedName(MEASURED_POWER_COLUMN_NAME)
	private float power;
	
	@DatabaseField(columnName = RSSI_COLUMN_NAME)
	@SerializedName(RSSI_COLUMN_NAME)
	private float rssi;
	
	@DatabaseField(columnName = ACCURACY_COLUMN_NAME)
	@SerializedName(ACCURACY_COLUMN_NAME)
	private float accuracy;
	
	@DatabaseField(columnName = TAG_NAME_COLUMN_NAME)
	@SerializedName(TAG_NAME_COLUMN_NAME)
	private String tagName;
	
	@DatabaseField(columnName = INSERT_DATE_COLUMN_NAME, dataType = DataType.DATE_LONG)
	@SerializedName(INSERT_DATE_COLUMN_NAME)
	private Date insertDate;
	
	public BeaconInfo()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}
	
	public BeaconInfo(IBeacon beacon, String tagName)
	{
		this.uuid = beacon.getProximityUuid();
		this.major = beacon.getMajor();
		this.minor = beacon.getMinor();
		this.address = beacon.getBluetoothAddress();
		this.power = beacon.getTxPower();
		this.rssi = beacon.getRssi();
		this.tagName = tagName;
		this.insertDate = new Date(System.currentTimeMillis());
	}

	public int getBeaconInfoId()
	{
		return beaconInfoId;
	}

	public void setBeaconInfoId(int beaconInfoId)
	{
		this.beaconInfoId = beaconInfoId;
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

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public float getPower()
	{
		return power;
	}

	public void setPower(float power)
	{
		this.power = power;
	}

	public float getRssi()
	{
		return rssi;
	}

	public void setRssi(float rssi)
	{
		this.rssi = rssi;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public Date getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(Date insertDate)
	{
		this.insertDate = insertDate;
	}
}
