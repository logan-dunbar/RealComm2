package com.openbox.realcomm2.database.models;

import java.util.Date;

import com.openbox.realcomm2.database.objects.Talk;
import com.openbox.realcomm2.database.objects.TalkTrack;
import com.openbox.realcomm2.utilities.helpers.DateHelper;

public class TalkModel
{
	private int venueId;
	private String name;
	private String description;
	private Date startTime;
	private Date endTime;
	private String talkTrack;
	
	public TalkModel(Talk talk, TalkTrack talkTrack)
	{
		this.venueId = talk.getVenueId();
		this.name = talk.getName();
		this.description = talk.getDescription();
		this.startTime = talk.getStartTime();
		this.endTime = talk.getEndTime();
		
		this.talkTrack = talkTrack.getName();
	}
	
	public int getVenueId()
	{
		return venueId;
	}

	public void setVenueId(int venueId)
	{
		this.venueId = venueId;
	}

	public Date getDate()
	{
		return DateHelper.getDateOnly(this.startTime);
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

	public String getTalkTrack()
	{
		return talkTrack;
	}

	public void setTalkTrack(String talkTrack)
	{
		this.talkTrack = talkTrack;
	}
}
