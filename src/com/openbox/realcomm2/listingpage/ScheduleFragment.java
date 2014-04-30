package com.openbox.realcomm2.listingpage;

import java.util.Date;
import java.util.TreeMap;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataInterface;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;

public class ScheduleFragment extends Fragment implements ScheduleDataChangedCallbacks
{
	private ScheduleDataInterface scheduleDataListener;
	private FragmentTabHost talkDayTabHost;
	private LinearLayout scheduleLayout;

	public static ScheduleFragment newInstance()
	{
		ScheduleFragment fragment = new ScheduleFragment();
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
		View view = inflater.inflate(R.layout.schedule_fragment, container, false);

		this.scheduleLayout = (LinearLayout) view.findViewById(R.id.scheduleLayout);

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onScheduleDataLoaded()
	{
		if (this.scheduleDataListener != null)
		{
			TreeMap<Date, String> distinctDateList = this.scheduleDataListener.getDistinctDayList();
			if (distinctDateList != null && distinctDateList.size() > 0)
			{
				// See if the tab host has already been created
				Boolean creating = false;
				this.talkDayTabHost = (FragmentTabHost) this.scheduleLayout.findViewById(R.id.talkDayTabHost);
				if (this.talkDayTabHost == null)
				{
					// Create it if not
					this.talkDayTabHost = new FragmentTabHost(getActivity());
					this.talkDayTabHost.setup(getActivity(), getChildFragmentManager(), R.id.scheduleTabContainer);
					this.talkDayTabHost.setId(R.id.talkDayTabHost);
					creating = true;
				}
				else
				{
					// Clear it if yes, because we have new data
					this.talkDayTabHost.clearAllTabs();
				}

				// For each day, create a tab holding a TalkDayFragment
				for (TreeMap.Entry<Date, String> entry : distinctDateList.entrySet())
				{
					Date talkDate = entry.getKey();
					String talkDateDisplayName = entry.getValue();

					Bundle args = new Bundle();
					args.putSerializable(TalkDayFragment.TALK_DATE_KEY, talkDate);
					TabSpec tabSepc = this.talkDayTabHost.newTabSpec(talkDateDisplayName).setIndicator(talkDateDisplayName);
					this.talkDayTabHost.addTab(tabSepc, TalkDayFragment.class, args);
				}

				// Add the view to the top of the layout
				// Happens at the end because we can't add until tabs have been initialized
				if (creating)
				{
					this.scheduleLayout.addView(this.talkDayTabHost, 0);
				}
			}
		}
	}
}
