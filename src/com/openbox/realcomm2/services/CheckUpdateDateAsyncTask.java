package com.openbox.realcomm2.services;

import com.openbox.realcomm2.base.BaseAsyncTask;
import com.openbox.realcomm2.utilities.helpers.DownloadDatabaseHelper;

import android.content.Context;

public class CheckUpdateDateAsyncTask extends BaseAsyncTask
{
	public static final String TASK_NAME = CheckUpdateDateAsyncTask.class.getName();
	private DownloadDatabaseHelper helper;

	private Boolean updateNeeded;
	private Boolean checkUpdateSucceeded;

	public Boolean getUpdateRequired()
	{
		return this.updateNeeded;
	}

	public Boolean getCheckUpdateSucceeded()
	{
		return checkUpdateSucceeded;
	}

	public CheckUpdateDateAsyncTask(WebService webService, Context context, String fetchMostRecentUpdateDateUrlString)
	{
		super(webService, TASK_NAME);
		this.helper = new DownloadDatabaseHelper(context, fetchMostRecentUpdateDateUrlString);
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		this.helper.checkUpdateNeeded();
		this.updateNeeded = this.helper.getUpdateNeeded();
		this.checkUpdateSucceeded = this.helper.getCheckUpdateSucceeded();
		return null;
	}
}
