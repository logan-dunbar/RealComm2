package com.openbox.realcomm2.base;

import com.openbox.realcomm2.utilities.interfaces.AsyncTaskInterface;

import android.os.AsyncTask;

public class BaseAsyncTask extends AsyncTask<Void, Void, Void>
{
	private AsyncTaskInterface taskInterface;
	public String taskName;
	
	public BaseAsyncTask(AsyncTaskInterface taskInterface, String taskName)
	{
		this.taskInterface = taskInterface;
		this.taskName = taskName;
	}
	
	public String getTaskName()
	{
		return this.taskName;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params)
	{
		// Override this to do background processing
		// (Made it return Void so that the developer is forced to make custom properties for return types)
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		// Should not need to override this, rather use onTaskComplete via the AsyncTaskInterface to do custom logic
		this.taskInterface.onTaskComplete(this);
		this.taskInterface.finishAsyncTask(this.taskName);
	}

	@Override
	protected void onCancelled()
	{
		// Should not need to override this, rather use onTaskCanceled via the AsyncTaskInterface to do custom logic
		this.taskInterface.onTaskCancelled(this);
		this.taskInterface.finishAsyncTask(this.taskName);
	}
}
