package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Venue")
public class Venue implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = 1889885233391662057L;
	
	public static final Class<?> ID_CLASS = Integer.class;
	
	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String VENUE_ID_COLUMN_NAME = "VenueId";
	public static final String ROOM_COLUMN_NAME = "Room";
	public static final String DESCRIPTION_COLUMN_NAME = "Description";
	
	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(columnName = VENUE_ID_COLUMN_NAME)
	@SerializedName(VENUE_ID_COLUMN_NAME)
	private int venueId;

	@DatabaseField(columnName = ROOM_COLUMN_NAME)
	@SerializedName(ROOM_COLUMN_NAME)
	private String room;

	@DatabaseField(columnName = DESCRIPTION_COLUMN_NAME)
	@SerializedName(DESCRIPTION_COLUMN_NAME)
	private String description;

	public Venue()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// VenueId
	public int getVenueId()
	{
		return venueId;
	}

	public void setVenueId(int venueId)
	{
		this.venueId = venueId;
	}

	// Room
	public String getRoom()
	{
		return room;
	}

	public void setRoom(String room)
	{
		this.room = room;
	}

	// Description
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
