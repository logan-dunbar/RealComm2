package com.openbox.realcomm2.database.models;

import java.util.List;

import com.openbox.realcomm2.database.objects.Venue;

public class VenueModel
{
	private int venueId;
	private String room;
	private List<TalkModel> talkList;

	public VenueModel(Venue venue, List<TalkModel> talkList)
	{
		this.venueId = venue.getVenueId();
		this.room = venue.getRoom();
		this.talkList = talkList;
	}

	public int getVenueId()
	{
		return venueId;
	}

	public void setVenueId(int venueId)
	{
		this.venueId = venueId;
	}

	public String getRoom()
	{
		return room;
	}

	public void setRoom(String room)
	{
		this.room = room;
	}

	public List<TalkModel> getTalkList()
	{
		return talkList;
	}

	public void setTalkList(List<TalkModel> talkList)
	{
		this.talkList = talkList;
	}
}
