package com.openbox.realcomm2.utilities.interfaces;

import com.openbox.realcomm2.base.BaseAsyncTask;

public interface AsyncTaskInterface
{
	void onTaskComplete(BaseAsyncTask task);
	void onTaskCancelled(BaseAsyncTask task);
	void finishAsyncTask(String taskName);
}
