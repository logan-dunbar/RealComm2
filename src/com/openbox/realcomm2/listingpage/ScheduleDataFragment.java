package com.openbox.realcomm2.listingpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.openbox.realcomm2.database.models.TalkDayModel;
import com.openbox.realcomm2.database.models.VenueModel;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataInterface;
import com.openbox.realcomm2.utilities.loaders.ScheduleLoader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class ScheduleDataFragment extends Fragment implements ScheduleDataInterface
{
	private static final String DAY_PREFIX = "Day ";
	private static final int SCHEDULE_LOADER_ID = 1;

	private ScheduleDataChangedCallbacks scheduleDataListener;
	private List<TalkDayModel> talkDayList = new ArrayList<TalkDayModel>();

	public static ScheduleDataFragment newInstance()
	{
		ScheduleDataFragment fragment = new ScheduleDataFragment();
		return fragment;
	}

	private void initScheduleLoader()
	{
		getLoaderManager().initLoader(SCHEDULE_LOADER_ID, null, this.scheduleLoaderCallbacks);
	}

	/**********************************************************************************************
	 * Lifecycle implements
	 **********************************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		initScheduleLoader();
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof ScheduleDataChangedCallbacks)
		{
			this.scheduleDataListener = (ScheduleDataChangedCallbacks) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		this.scheduleDataListener = null;
	}

	/**********************************************************************************************
	 * Schedule Data Interface Implements
	 **********************************************************************************************/
	@Override
	public void refreshData()
	{
		Loader<List<TalkDayModel>> scheduleLoader = getLoaderManager().getLoader(SCHEDULE_LOADER_ID);
		if (scheduleLoader == null)
		{
			initScheduleLoader();
		}
		else
		{
			scheduleLoader.onContentChanged();
		}
	}

	public TreeMap<Date, String> getDistinctDayList()
	{
		// TreeMap used because it is automatically sorted on key
		// TODO might not need if sorting manually, ArrayList should work fine

		// Get list of dates
		List<Date> dateList = new ArrayList<>();
		for (TalkDayModel talkDay : this.talkDayList)
		{
			dateList.add(talkDay.getDate());
		}

		// Sort dates in ascending order
		Collections.sort(dateList);

		// Create the distinct list, including its "display name" (Day 1, Day 2, etc.)
		TreeMap<Date, String> distinctDateList = new TreeMap<>();
		for (int i = 0; i < dateList.size(); i++)
		{
			// + 1 to display 0 indexed number
			distinctDateList.put(dateList.get(i), DAY_PREFIX + (i + 1));
		}

		return distinctDateList;
	}

	@Override
	public List<TalkDayModel> getTalkDayList()
	{
		return this.talkDayList;
	}

	@Override
	public TalkDayModel getTalkDay(Date talkDate)
	{
		for (TalkDayModel talkDayModel : this.talkDayList)
		{
			if (talkDayModel.getDate().equals(talkDate))
			{
				return talkDayModel;
			}
		}

		return null;
	}

	@Override
	public VenueModel getVenueForDate(Date talkDate, int venueId)
	{
		TalkDayModel talkDay = getTalkDay(talkDate);

		if (talkDay != null)
		{
			for (VenueModel venue : talkDay.getVenueList())
			{
				if (venue.getVenueId() == venueId)
				{
					return venue;
				}
			}
		}

		return null;
	}

	/**********************************************************************************************
	 * Loader Callbacks
	 **********************************************************************************************/
	private LoaderCallbacks<List<TalkDayModel>> scheduleLoaderCallbacks = new LoaderCallbacks<List<TalkDayModel>>()
	{
		@Override
		public Loader<List<TalkDayModel>> onCreateLoader(int loaderId, Bundle bundle)
		{
			return new ScheduleLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<List<TalkDayModel>> loader, List<TalkDayModel> talkDayList)
		{
			ScheduleDataFragment.this.talkDayList = talkDayList;
			if (ScheduleDataFragment.this.scheduleDataListener != null)
			{
				ScheduleDataFragment.this.scheduleDataListener.onScheduleDataLoaded();
			}
		}

		@Override
		public void onLoaderReset(Loader<List<TalkDayModel>> loader)
		{
			ScheduleDataFragment.this.talkDayList = new ArrayList<TalkDayModel>();
		}
	};
}
