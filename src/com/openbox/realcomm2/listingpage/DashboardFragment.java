package com.openbox.realcomm2.listingpage;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.utilities.interfaces.AppModeChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BoothDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataChangedCallbacks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DashboardFragment extends Fragment implements ScheduleDataChangedCallbacks, AppModeChangedCallbacks, BoothDataChangedCallbacks
{
	// TODO might not need
	private ScheduleDataChangedCallbacks scheduleDataChangedListener;
	private AppModeChangedCallbacks appModeChangedListener;
	private BoothDataChangedCallbacks boothDataChangedListener;

	public static DashboardFragment newInstance()
	{
		DashboardFragment fragment = new DashboardFragment();
		return fragment;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		this.scheduleDataChangedListener = null;
		this.appModeChangedListener = null;
		this.boothDataChangedListener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.dashboard_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		createConnectionStatusFragment();
		createProximityFragment();
		createScheduleFragment();
	}

	private void createConnectionStatusFragment()
	{
		ConnectionStatusFragment statusFragment = (ConnectionStatusFragment) getChildFragmentManager().findFragmentById(R.id.connectionStatusContainer);
		if (statusFragment == null)
		{
			statusFragment = ConnectionStatusFragment.newInstance();
			getChildFragmentManager().beginTransaction().add(R.id.connectionStatusContainer, statusFragment).commit();
		}
		
		this.appModeChangedListener = statusFragment;
	}

	private void createProximityFragment()
	{
		ProximityFragment proximityFragment = (ProximityFragment) getChildFragmentManager().findFragmentById(R.id.proximityContainer);
		if (proximityFragment == null)
		{
			proximityFragment = ProximityFragment.newInstance();
			getChildFragmentManager().beginTransaction().add(R.id.proximityContainer, proximityFragment).commit();
		}
		
		this.boothDataChangedListener = proximityFragment;
	}

	private void createScheduleFragment()
	{
		ScheduleFragment scheduleFragment = (ScheduleFragment) getChildFragmentManager().findFragmentById(R.id.scheduleFragmentContainer);
		if (scheduleFragment == null)
		{
			scheduleFragment = ScheduleFragment.newInstance();
			getChildFragmentManager().beginTransaction().add(R.id.scheduleFragmentContainer, scheduleFragment).commit();
		}
		
		this.scheduleDataChangedListener = scheduleFragment;
	}

	@Override
	public void onScheduleDataLoaded()
	{
		if (this.scheduleDataChangedListener != null)
		{
			this.scheduleDataChangedListener.onScheduleDataLoaded();
		}
	}

	@Override
	public void onAppModeChanged()
	{
		if (this.appModeChangedListener != null)
		{
			this.appModeChangedListener.onAppModeChanged();
		}
	}

	@Override
	public void onBoothDataLoaded()
	{
		if (this.boothDataChangedListener != null)
		{
			this.boothDataChangedListener.onBoothDataLoaded();
		}
	}

	@Override
	public void onCompanyDataLoaded()
	{
		if (this.boothDataChangedListener != null)
		{
			this.boothDataChangedListener.onCompanyDataLoaded();
		}
	}

	@Override
	public void onBoothDataUpdated()
	{
		if (this.boothDataChangedListener != null)
		{
			this.boothDataChangedListener.onBoothDataUpdated();
		}
	}
}
