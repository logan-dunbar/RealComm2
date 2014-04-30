package com.openbox.realcomm2.listingpage;

import java.util.TimerTask;

import com.openbox.realcomm2.utilities.interfaces.TimerTaskCallbacks;

public class ViewTimerTask extends TimerTask
{
	private TimerTaskCallbacks listener;

	public ViewTimerTask(TimerTaskCallbacks listener)
	{
		this.listener = listener;
	}

	@Override
	public void run()
	{
		// This is async, so must use runOnUiThread in listener
		listener.onTimerTick();
	}
}
