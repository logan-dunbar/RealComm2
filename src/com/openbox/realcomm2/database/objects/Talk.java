package com.openbox.realcomm2.database.objects;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Talk")
public class Talk implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = -8607377396768119733L;

	public static final Class<?> ID_CLASS = Integer.class;
	
	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String TALK_ID_COLUMN_NAME = "TalkId";
	public static final String NAME_COLUMN_NAME = "Name";
	public static final String DESCRIPTION_COLUMN_NAME = "Description";
	public static final String START_TIME_COLUMN_NAME = "StartTime";
	public static final String END_TIME_COLUMN_NAME = "EndTime";

	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(id = true, columnName = TALK_ID_COLUMN_NAME)
	@SerializedName(TALK_ID_COLUMN_NAME)
	private int talkId;

	@DatabaseField(columnName = Venue.VENUE_ID_COLUMN_NAME)
	@SerializedName(Venue.VENUE_ID_COLUMN_NAME)
	private int venueId;

	@DatabaseField(columnName = TalkTrack.TALK_TRACK_ID_COLUMN_NAME)
	@SerializedName(TalkTrack.TALK_TRACK_ID_COLUMN_NAME)
	private int talkTrackId;

	@DatabaseField(columnName = NAME_COLUMN_NAME)
	@SerializedName(NAME_COLUMN_NAME)
	private String name;

	@DatabaseField(columnName = DESCRIPTION_COLUMN_NAME)
	@SerializedName(DESCRIPTION_COLUMN_NAME)
	private String description;

	@DatabaseField(columnName = START_TIME_COLUMN_NAME)
	@SerializedName(START_TIME_COLUMN_NAME)
	private Date startTime;

	@DatabaseField(columnName = END_TIME_COLUMN_NAME)
	@SerializedName(END_TIME_COLUMN_NAME)
	private Date endTime;
	
	public Talk()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}
	
	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	public int getTalkId()
	{
		return talkId;
	}

	public void setTalkId(int talkId)
	{
		this.talkId = talkId;
	}

	public int getVenueId()
	{
		return venueId;
	}

	public void setVenueId(int venueId)
	{
		this.venueId = venueId;
	}

	public int getTalkTrackId()
	{
		return talkTrackId;
	}

	public void setTalkTrackId(int talkTrackId)
	{
		this.talkTrackId = talkTrackId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
}
