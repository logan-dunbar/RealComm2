package com.openbox.realcomm2.utilities.loaders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openbox.realcomm2.database.DatabaseManager;
import com.openbox.realcomm2.database.models.TalkDayModel;
import com.openbox.realcomm2.database.models.TalkModel;
import com.openbox.realcomm2.database.models.VenueModel;
import com.openbox.realcomm2.database.objects.Talk;
import com.openbox.realcomm2.database.objects.TalkTrack;
import com.openbox.realcomm2.database.objects.Venue;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ScheduleLoader extends AsyncTaskLoader<List<TalkDayModel>>
{
	private List<TalkDayModel> talkDayList;

	public ScheduleLoader(Context context)
	{
		super(context);
	}

	@Override
	protected void onStartLoading()
	{
		// If we currently have a result available, deliver it immediately.
		if (this.talkDayList != null)
		{
			deliverResult(talkDayList);
		}
		else
		{
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading()
	{
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onContentChanged()
	{
		// We were notified that content has changed, force a load.
		forceLoad();
	}

	@Override
	public void reset()
	{
		super.reset();

		// Ensure the loader is stopped
		onStopLoading();

		if (this.talkDayList != null)
		{
			this.talkDayList = null;
		}
	}

	@Override
	public void deliverResult(List<TalkDayModel> data)
	{
		this.talkDayList = data;

		// If the Loader is currently started, we can immediately deliver its results.
		if (isStarted())
		{
			super.deliverResult(data);
		}
	}

	@Override
	public List<TalkDayModel> loadInBackground()
	{
		List<TalkDayModel> talkDayList = new ArrayList<TalkDayModel>();

		try
		{
			// Fetch all data objects
			List<Venue> venueList = DatabaseManager.getInstance().getAll(Venue.class);
			List<TalkTrack> talkTrackList = DatabaseManager.getInstance().getAll(TalkTrack.class);
			List<Talk> talkList = DatabaseManager.getInstance().getAll(Talk.class);

			// Initialize full list of TalkModels
			List<TalkModel> fullTalkModelList = new ArrayList<TalkModel>();
			for (Talk talk : talkList)
			{
				for (TalkTrack talkTrack : talkTrackList)
				{
					// TODO: if there is no match?
					if (talk.getTalkTrackId() == talkTrack.getTalkTrackId())
					{
						fullTalkModelList.add(new TalkModel(talk, talkTrack));
						break;
					}
				}
			}

			// Create a distinct list of dates
			List<Date> dayDateList = new ArrayList<Date>();
			for (TalkModel talkModel : fullTalkModelList)
			{
				Date date = talkModel.getDate();
				if (!dayDateList.contains(date))
				{
					dayDateList.add(date);
				}
			}

			// Initialize talkDayList
			for (Date date : dayDateList)
			{
				talkDayList.add(new TalkDayModel(date));
			}

			// Initialize each days talks, by venue
			for (TalkDayModel talkDayModel : talkDayList)
			{
				// Find talks for the day
				List<TalkModel> dayTalkModelList = new ArrayList<TalkModel>();
				for (TalkModel talkModel : fullTalkModelList)
				{
					if (talkDayModel.getDate().equals(talkModel.getDate()))
					{
						dayTalkModelList.add(talkModel);
					}
				}

				// Organize by venue
				List<VenueModel> venueModelList = new ArrayList<VenueModel>();
				for (Venue venue : venueList)
				{
					List<TalkModel> dayTalkVenueModelList = new ArrayList<TalkModel>();
					for (TalkModel talkModel : dayTalkModelList)
					{
						if (venue.getVenueId() == talkModel.getVenueId())
						{
							dayTalkVenueModelList.add(talkModel);
						}
					}

					// Only add the venue if it has talks for the day
					if (dayTalkVenueModelList.size() > 0)
					{
						venueModelList.add(new VenueModel(venue, dayTalkVenueModelList));
					}
				}

				talkDayModel.setVenueList(venueModelList);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return talkDayList;
	}
}
