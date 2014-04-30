package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "TalkTrack")
public class TalkTrack implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = -6210861993284633017L;
	
	public static final Class<?> ID_CLASS = Integer.class;

	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String TALK_TRACK_ID_COLUMN_NAME = "TalkTrackId";
	public static final String NAME_COLUMN_NAME = "Name";
	public static final String DESCRIPTION_COLUMN_NAME = "Description";
	
	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(columnName = TALK_TRACK_ID_COLUMN_NAME)
	@SerializedName(TALK_TRACK_ID_COLUMN_NAME)
	private int talkTrackId;

	@DatabaseField(columnName = NAME_COLUMN_NAME)
	@SerializedName(NAME_COLUMN_NAME)
	private String name;

	@DatabaseField(columnName = DESCRIPTION_COLUMN_NAME)
	@SerializedName(DESCRIPTION_COLUMN_NAME)
	private String description;
	
	public TalkTrack()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// TalkTrackId
	public int getTalkTrackId()
	{
		return talkTrackId;
	}

	public void setTalkTrackId(int talkTrackId)
	{
		this.talkTrackId = talkTrackId;
	}

	// Name
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
