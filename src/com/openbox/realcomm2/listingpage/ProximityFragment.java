package com.openbox.realcomm2.listingpage;

import java.util.Map;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.utilities.enums.AppMode;
import com.openbox.realcomm2.utilities.enums.ProximityRegion;
import com.openbox.realcomm2.utilities.interfaces.AppModeChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.AppModeInterface;
import com.openbox.realcomm2.utilities.interfaces.BoothDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BoothDataInterface;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProximityFragment extends Fragment implements BoothDataChangedCallbacks, AppModeChangedCallbacks
{
	private BoothDataInterface boothDataListener;
	private AppModeInterface appModeListener;

	private Resources resources;

	private TextView immediateTextView;
	private TextView nearTextView;
	private TextView farTextView;

	public static ProximityFragment newInstance()
	{
		ProximityFragment fragment = new ProximityFragment();
		return fragment;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof BoothDataInterface)
		{
			this.boothDataListener = (BoothDataInterface) activity;
		}

		if (activity instanceof AppModeInterface)
		{
			this.appModeListener = (AppModeInterface) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.proximity_fragment, container, false);

		this.resources = getActivity().getResources();

		this.immediateTextView = (TextView) view.findViewById(R.id.immediateTextView);
		this.nearTextView = (TextView) view.findViewById(R.id.nearTextView);
		this.farTextView = (TextView) view.findViewById(R.id.FarTextView);

		// TODO Maybe set it to grey and then when it comes online/loaded changes color?
		// updateView();

		return view;
	}

	private void updateView()
	{
		if (this.boothDataListener != null && this.appModeListener != null)
		{
			if (this.appModeListener.getCurrentAppMode() == AppMode.Offline)
			{
				updateTextView(this.immediateTextView, ProximityRegion.OutOfRange, 0);
				updateTextView(this.nearTextView, ProximityRegion.OutOfRange, 0);
				updateTextView(this.farTextView, ProximityRegion.OutOfRange, 0);
			}
			else
			{
				Map<ProximityRegion, Integer> proximityCounts = this.boothDataListener.getBoothProximityCounts();
				if (proximityCounts != null)
				{
					updateTextView(this.immediateTextView, ProximityRegion.Immediate, proximityCounts.get(ProximityRegion.Immediate));
					updateTextView(this.nearTextView, ProximityRegion.Near, proximityCounts.get(ProximityRegion.Near));
					updateTextView(this.farTextView, ProximityRegion.Far, proximityCounts.get(ProximityRegion.Far));
				}
			}
		}
	}
	
	private void updateTextView(TextView textView, ProximityRegion proximityRegion, int count)
	{
		textView.setText(String.valueOf(count));
		GradientDrawable bg = (GradientDrawable) textView.getBackground();
		bg.setColor(this.resources.getColor(proximityRegion.getColorId()));
	}

	@Override
	public void onBoothDataLoaded()
	{
		updateView();
	}

	@Override
	public void onCompanyDataLoaded()
	{
		updateView();
	}

	@Override
	public void onBoothDataUpdated()
	{
		updateView();
	}

	@Override
	public void onAppModeChanged()
	{
		updateView();
	}

}
