package com.openbox.realcomm2.utilities.interfaces;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.openbox.realcomm2.database.models.TalkDayModel;
import com.openbox.realcomm2.database.models.VenueModel;

public interface ScheduleDataInterface
{
	void refreshData();
	
	TreeMap<Date, String> getDistinctDayList();

	List<TalkDayModel> getTalkDayList();

	TalkDayModel getTalkDay(Date talkDate);

	VenueModel getVenueForDate(Date talkDate, int venueId);
}
