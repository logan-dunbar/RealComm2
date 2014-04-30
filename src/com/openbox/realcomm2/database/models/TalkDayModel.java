package com.openbox.realcomm2.database.models;

import java.util.Date;
import java.util.List;

public class TalkDayModel
{
	private Date date;
	private List<VenueModel> venueList;

	public TalkDayModel(Date date)
	{
		this.date = date;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public List<VenueModel> getVenueList()
	{
		return venueList;
	}

	public void setVenueList(List<VenueModel> venueList)
	{
		this.venueList = venueList;
	}

}
