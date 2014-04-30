package com.openbox.realcomm2.services;

import android.content.Context;

import com.openbox.realcomm2.base.BaseAsyncTask;
import com.openbox.realcomm2.utilities.helpers.DownloadDatabaseHelper;
import com.openbox.realcomm2.utilities.interfaces.AsyncTaskInterface;

public class DownloadDatabaseAsyncTask extends BaseAsyncTask
{
	public static final String TASK_NAME = DownloadDatabaseAsyncTask.class.getName();
	private DownloadDatabaseHelper helper;

	private Boolean downloadAndWriteSucceeded;

	public Boolean getDownloadAndWriteSucceeded()
	{
		return this.downloadAndWriteSucceeded;
	}

	public DownloadDatabaseAsyncTask(AsyncTaskInterface taskInterface, Context context, String fetchDatabaseUrlString)
	{
		super(taskInterface, TASK_NAME);
		this.helper = new DownloadDatabaseHelper(context, fetchDatabaseUrlString);
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		this.helper.downloadDatabase();
		if (this.helper.getDownloadDatabaseSucceeded())
		{
			this.helper.writeDatabase();
			this.downloadAndWriteSucceeded = this.helper.getDownloadDatabaseSucceeded();
		}

		return null;
	}
}
