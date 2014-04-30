package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BoothContact")
public class BoothContact implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = -4687396465975262898L;
	
	public static final Class<?> ID_CLASS = Integer.class;

	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String BOOTH_CONTACT_ID_COLUMN_NAME = "BoothContactId";

	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(id = true, columnName = BOOTH_CONTACT_ID_COLUMN_NAME)
	@SerializedName(BOOTH_CONTACT_ID_COLUMN_NAME)
	private int boothContactId;

	@DatabaseField(columnName = Booth.BOOTH_ID_COLUMN_NAME)
	@SerializedName(Booth.BOOTH_ID_COLUMN_NAME)
	private int boothId;

	@DatabaseField(columnName = Contact.CONTACT_ID_COLUMN_NAME)
	@SerializedName(Contact.CONTACT_ID_COLUMN_NAME)
	private int contactId;

	public BoothContact()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// BoothContactId
	public int getBoothContactId()
	{
		return this.boothContactId;
	}

	public void setBoothContactId(int boothContactId)
	{
		this.boothContactId = boothContactId;
	}

	// BoothId
	public int getBoothId()
	{
		return this.boothId;
	}

	public void setBoothId(int boothId)
	{
		this.boothId = boothId;
	}

	// ContactId
	public int getContactId()
	{
		return this.contactId;
	}

	public void setContactId(int contactId)
	{
		this.contactId = contactId;
	}
}
