package com.openbox.realcomm2.listingpage;

import java.util.Date;
import com.openbox.realcomm2.R;
import com.openbox.realcomm2.database.models.TalkDayModel;
import com.openbox.realcomm2.utilities.adapters.VenueFragmentAdapter;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataInterface;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TalkDayFragment extends Fragment
{
	public static final String TALK_DATE_KEY = "talkDateKey";

	private ScheduleDataInterface scheduleDataListener;
	private ViewPager venuePager;

	public static TalkDayFragment newInstance()
	{
		TalkDayFragment fragment = new TalkDayFragment();
		return fragment;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof ScheduleDataInterface)
		{
			this.scheduleDataListener = (ScheduleDataInterface) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.talk_day_fragment, container, false);

		this.venuePager = (ViewPager) view.findViewById(R.id.venuePager);
		VenueFragmentAdapter venueAdapter = new VenueFragmentAdapter(getChildFragmentManager());
		this.venuePager.setAdapter(venueAdapter);
		this.venuePager.setOffscreenPageLimit(5);

		if (this.scheduleDataListener != null)
		{
			Date talkDate = (Date) getArguments().getSerializable(TALK_DATE_KEY);
			TalkDayModel talkDay = this.scheduleDataListener.getTalkDay(talkDate);
			if (talkDay != null)
			{
				venueAdapter.setItems(talkDate, talkDay.getVenueList());
			}
		}

		return view;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		
		this.venuePager.setCurrentItem(0);
	}
}
