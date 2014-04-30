package com.openbox.realcomm2.listingpage;

import java.util.Date;
import com.openbox.realcomm2.R;
import com.openbox.realcomm2.database.models.VenueModel;
import com.openbox.realcomm2.utilities.adapters.TalkListAdapter;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataInterface;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class VenueFragment extends Fragment
{
	private static final String VENUE_ID_KEY = "venueIdKey";
	
	private ScheduleDataInterface scheduleDataListener;
	
	private TextView venueTextView;
	private ListView talkListView;
	private TalkListAdapter talkAdapter;
	
	public static VenueFragment newInstance(Date talkDate, int venueId)
	{
		VenueFragment fragment = new VenueFragment();
		
		Bundle args = new Bundle();
		args.putSerializable(TalkDayFragment.TALK_DATE_KEY, talkDate);
		args.putInt(VENUE_ID_KEY, venueId);
		fragment.setArguments(args);
		
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
	public void onDetach()
	{
		super.onDetach();
		this.scheduleDataListener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.venue_fragment, container, false);
		
		this.venueTextView = (TextView) view.findViewById(R.id.venueTextView);
		this.talkListView = (ListView) view.findViewById(R.id.talkListView);
		this.talkAdapter = new TalkListAdapter(getActivity());
		this.talkListView.setAdapter(this.talkAdapter);
		
		if (this.scheduleDataListener != null)
		{
			Bundle args = getArguments();
			VenueModel venue = this.scheduleDataListener.getVenueForDate((Date) args.getSerializable(TalkDayFragment.TALK_DATE_KEY), args.getInt(VENUE_ID_KEY));
			if (venue != null)
			{
				this.venueTextView.setText(venue.getRoom());
				this.talkAdapter.setItems(venue.getTalkList());
			}
		}
		
		return view;
	}
}
