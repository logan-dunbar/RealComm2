package com.openbox.realcomm2.listingpage;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.utilities.interfaces.AppModeChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.AppModeInterface;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConnectionStatusFragment extends Fragment implements AppModeChangedCallbacks
{
	private static final String CONNECTION_STATUS_PREFIX = "Status: ";

	private AppModeInterface appModeListener;

	private TextView connectionStatusTextView;

	public static ConnectionStatusFragment newInstance()
	{
		ConnectionStatusFragment fragment = new ConnectionStatusFragment();
		return fragment;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof AppModeInterface)
		{
			this.appModeListener = (AppModeInterface) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();

		// Clean up
		this.appModeListener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.connection_status_fragment, container, false);

		this.connectionStatusTextView = (TextView) view.findViewById(R.id.connectionStatusTextView);

		updateView();

		return view;
	}

	private void updateView()
	{
		if (this.appModeListener != null && this.appModeListener.getCurrentAppMode() != null)
		{
			// TODO maybe add color or something
			this.connectionStatusTextView.setText(CONNECTION_STATUS_PREFIX + this.appModeListener.getCurrentAppMode().getDisplayName());
		}
	}

	@Override
	public void onAppModeChanged()
	{
		updateView();
	}
}
