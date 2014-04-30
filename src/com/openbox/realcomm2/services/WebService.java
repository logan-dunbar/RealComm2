package com.openbox.realcomm2.services;

import com.openbox.realcomm2.application.RealCommApplication;
import com.openbox.realcomm2.base.BaseAsyncTask;
import com.openbox.realcomm2.utilities.interfaces.AsyncTaskInterface;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class WebService extends Service implements AsyncTaskInterface
{
	public static final String ROOT_API_URL = "http://obtestweb.openbox.local/RealComm/api/Default/";
	public static final String FETCH_MOST_RECENT_UPDATE_DATE_API_URL = ROOT_API_URL + "FetchMostRecentUpdateDate";
	public static final String FETCH_DATABASE_API_URL = ROOT_API_URL + "FetchRealCommDatabase";

	public static final String CHECK_UPDATE_INTENT = "checkUpdateIntent";
	public static final String DOWNLOAD_DATABASE_INTENT = "downloadDatabaseIntent";

	private IBinder binder = new WebServiceBinder();

	private CheckUpdateDateAsyncTask checkUpdateTask;
	private DownloadDatabaseAsyncTask downloadTask;

	public class WebServiceBinder extends Binder
	{
		public WebService getService()
		{
			return WebService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// Check the network connectivity situation
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		// Can maybe do something here with the different connections
		// NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (activeNetworkInfo == null || !activeNetworkInfo.isConnected())
		{
			// Not connected, do nothing?
		}
		else if (activeNetworkInfo.isConnected())
		{
			// We are connected, check if we need to update (maybe do something with waiting for WiFi or Roaming?)
			checkUpdateNeeded();
		}

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return this.binder;
	}

	private void checkUpdateNeeded()
	{
		if (this.checkUpdateTask == null)
		{
			this.checkUpdateTask = new CheckUpdateDateAsyncTask(this, this, FETCH_MOST_RECENT_UPDATE_DATE_API_URL);
			this.checkUpdateTask.execute();
		}
	}

	public void downloadDatabase()
	{
		if (this.downloadTask == null)
		{
			this.downloadTask = new DownloadDatabaseAsyncTask(this, this, FETCH_DATABASE_API_URL);
			this.downloadTask.execute();
		}
	}

	@Override
	public void onTaskComplete(BaseAsyncTask task)
	{
		if (task == this.checkUpdateTask)
		{
			if (this.checkUpdateTask.getCheckUpdateSucceeded())
			{
				RealCommApplication.updateNeeded = this.checkUpdateTask.getUpdateRequired();
			}
			else
			{
				// TODO: Check update didn't work correctly, maybe do something here, try again in a few mins? alert
				// user?
			}

			this.checkUpdateTask = null;

			// Make sure to always signal task complete (even if it failed)
			Intent intent = new Intent(CHECK_UPDATE_INTENT);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

			stopSelf();
		}
		else if (task == this.downloadTask)
		{
			if (this.downloadTask.getDownloadAndWriteSucceeded())
			{
				// Everything done, clean up
				RealCommApplication.updateNeeded = false;
			}
			else
			{
				// download and write failed, do something, alert user?
			}

			this.downloadTask = null;

			// Make sure to always signal task complete (even if it failed)
			Intent intent = new Intent(DOWNLOAD_DATABASE_INTENT);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		}
	}

	@Override
	public void onTaskCancelled(BaseAsyncTask task)
	{
	}

	@Override
	public void finishAsyncTask(String taskName)
	{
	}
}
