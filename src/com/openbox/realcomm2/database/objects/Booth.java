package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Booth")
public class Booth implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = 3994085026109724657L;
	
	public static final Class<?> ID_CLASS = Integer.class;

	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String BOOTH_ID_COLUMN_NAME = "BoothId";
	public static final String MAJOR_COLUMN_NAME = "Major";
	public static final String MINOR_COLUMN_NAME = "Minor";
	public static final String UUID_COLUMN_NAME = "UUID";
	public static final String NAME_COLUMN_NAME = "Name";
	public static final String BOOTH_NUMBER_COLUMN_NAME = "BoothNumber";

	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(id = true, columnName = BOOTH_ID_COLUMN_NAME)
	@SerializedName(BOOTH_ID_COLUMN_NAME)
	private int boothId;

	@DatabaseField(columnName = UUID_COLUMN_NAME)
	@SerializedName(UUID_COLUMN_NAME)
	private String UUID;

	@DatabaseField(columnName = MAJOR_COLUMN_NAME)
	@SerializedName(MAJOR_COLUMN_NAME)
	private int major;

	@DatabaseField(columnName = MINOR_COLUMN_NAME)
	@SerializedName(MINOR_COLUMN_NAME)
	private int minor;

	@DatabaseField(columnName = NAME_COLUMN_NAME)
	@SerializedName(NAME_COLUMN_NAME)
	private String name;
	
	@DatabaseField(columnName = BOOTH_NUMBER_COLUMN_NAME)
	@SerializedName(BOOTH_NUMBER_COLUMN_NAME)
	private int boothNumber;

	public Booth()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// BoothId
	public int getBoothId()
	{
		return this.boothId;
	}

	public void setBoothId(int boothId)
	{
		this.boothId = boothId;
	}

	// UUID
	public String getUUID()
	{
		return this.UUID;
	}

	public void setUUID(String UUID)
	{
		this.UUID = UUID;
	}
	
	// Major
	public int getMajor()
	{
		return this.major;
	}

	public void setMajor(int major)
	{
		this.major = major;
	}

	// Minor
	public int getMinor()
	{
		return this.minor;
	}

	public void setMinor(int minor)
	{
		this.minor = minor;
	}

	// Name
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// Booth Number
	public int getBoothNumber()
	{
		return boothNumber;
	}

	public void setBoothNumber(int boothNumber)
	{
		this.boothNumber = boothNumber;
	}

	@Override
	public String toString()
	{
		return this.name + ": " + this.major + "-" + this.minor;
	}
}
