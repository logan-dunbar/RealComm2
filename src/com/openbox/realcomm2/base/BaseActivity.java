package com.openbox.realcomm2.base;

import java.util.Dictionary;
import java.util.Hashtable;


import com.openbox.realcomm2.R;
import com.openbox.realcomm2.application.RealCommApplication;
import com.openbox.realcomm2.datacapture.DataCaptureActivity;
import com.openbox.realcomm2.services.WebService;
import com.openbox.realcomm2.settingspage.SettingsPageActivity;
import com.openbox.realcomm2.utilities.helpers.ToastHelper;
import com.openbox.realcomm2.utilities.interfaces.AsyncTaskInterface;
import com.openbox.realcomm2.utilities.interfaces.DatabaseSyncReceiverInterface;
import com.openbox.realcomm2.utilities.interfaces.WebServiceConnectionInterface;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.AsyncTask.Status;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class BaseActivity extends FragmentActivity implements AsyncTaskInterface, WebServiceConnectionInterface, DatabaseSyncReceiverInterface
{
	/**********************************************************************************************
	 * Fields
	 **********************************************************************************************/
	private WebService webService;
	private MenuItem updateMenuItem;
	private ActionBar actionBar;
	private static Dictionary<String, BaseAsyncTask> asyncTaskDictionary = new Hashtable<String, BaseAsyncTask>();

	/**********************************************************************************************
	 * Broadcast Receivers
	 **********************************************************************************************/
	private BroadcastReceiver checkUpdateBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			BaseActivity.this.onCheckUpdateReceive(intent);
		}
	};

	private BroadcastReceiver downloadDatabaseBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			BaseActivity.this.onDownloadDatabaseReceive(intent);
		}
	};

	/**********************************************************************************************
	 * Service Connections
	 **********************************************************************************************/
	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder)
		{
			BaseActivity.this.webService = ((WebService.WebServiceBinder) binder).getService();
			BaseActivity.this.onServiceConnected(name);
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			BaseActivity.this.onServiceDisconnected(name);
			BaseActivity.this.webService = null;
		}
	};

	/**********************************************************************************************
	 * Getters and setters
	 **********************************************************************************************/
	public WebService getWebService()
	{
		return webService;
	}

	/**********************************************************************************************
	 * Lifecycle Implements
	 **********************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.actionBar = getActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Careful when this is called in the Lifecycle
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		this.updateMenuItem = menu.findItem(R.id.refreshMenuItem);
		updateUpdateMenuItemVisibility();
		return super.onCreateOptionsMenu(menu);
	}

	/**********************************************************************************************
	 * Click Implements
	 **********************************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.refreshMenuItem:
				this.updateMenuItem.setEnabled(false);
				downloadDatabase();
				break;
			case R.id.settingsMenuItem:
				startSettingsActivity();
				break;
			case R.id.dataCaptureMenuItem:
				startDataCaptureActivity();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**********************************************************************************************
	 * AsyncTaskInterface Implements
	 **********************************************************************************************/
	@Override
	public void finishAsyncTask(String taskName)
	{
		asyncTaskDictionary.remove(taskName);
	}

	@Override
	public void onTaskComplete(BaseAsyncTask task)
	{
		// This should be overridden when using AsyncTask in a custom activity
	}

	@Override
	public void onTaskCancelled(BaseAsyncTask task)
	{
		// This should be overridden when using AsyncTask in a custom activity
	}

	/**********************************************************************************************
	 * WebServiceConnectionInterface Implements
	 **********************************************************************************************/
	@Override
	public void onServiceConnected(ComponentName name)
	{
		// TODO LD - Might need to differentiate here?
		getWebService().downloadDatabase();
	}

	@Override
	public void onServiceDisconnected(ComponentName name)
	{
		// This can be overridden and used as pleased
	}

	/**********************************************************************************************
	 * AsyncTask Management Methods
	 **********************************************************************************************/
	protected void startAsyncTask(BaseAsyncTask newTask)
	{
		String taskName = newTask.getTaskName();
		BaseAsyncTask task = asyncTaskDictionary.get(taskName);
		if (task == null)
		{
			task = newTask;
			asyncTaskDictionary.put(taskName, task);
		}

		// Only run if task hasn't started yet
		if (task.getStatus() == Status.PENDING)
		{
			task.execute();
		}
	}

	protected void cancelAsyncTask(String taskName)
	{
		// TODO LD - Maybe a bool return to see if it did actually cancel or not (probably not though)
		BaseAsyncTask task = asyncTaskDictionary.get(taskName);
		if (task != null)
		{
			// TODO LD - Check the param mayInterruptIfRunning, not sure what it does
			task.cancel(true);
		}
	}

	/**********************************************************************************************
	 * WebService Methods
	 **********************************************************************************************/
	protected void bindWebService()
	{
		Intent service = new Intent(this, WebService.class);
		bindService(service, this.serviceConnection, Context.BIND_AUTO_CREATE);
	}

	protected void unbindWebService()
	{
		unbindService(this.serviceConnection);
	}

	/**********************************************************************************************
	 * Broadcast Receiver Methods
	 **********************************************************************************************/
	@Override
	public void onCheckUpdateReceive(Intent intent)
	{
		// TODO Override to implement when checking update is complete, super() must be called last
		updateUpdateMenuItemVisibility();
	}

	@Override
	public void onDownloadDatabaseReceive(Intent intent)
	{
		// TODO Override to implement when downloading com.openbox.realcomm.database is complete, super() must be called last
		updateUpdateMenuItemVisibility();

		unbindDownloadDatabaseReceiver();
		unbindWebService();

		this.updateMenuItem.setEnabled(true);
		ToastHelper.showLongMessage(this, "Sync complete");
	}

	@Override
	public void bindCheckUpdateReceiver()
	{
		LocalBroadcastManager.getInstance(this).registerReceiver(checkUpdateBroadcastReceiver, new IntentFilter(WebService.CHECK_UPDATE_INTENT));
	}

	@Override
	public void unbindCheckUpdateReceiver()
	{
		LocalBroadcastManager.getInstance(this).unregisterReceiver(checkUpdateBroadcastReceiver);
	}

	@Override
	public void bindDownloadDatabaseRecever()
	{
		LocalBroadcastManager.getInstance(this).registerReceiver(downloadDatabaseBroadcastReceiver, new IntentFilter(WebService.DOWNLOAD_DATABASE_INTENT));
	}

	@Override
	public void unbindDownloadDatabaseReceiver()
	{
		LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadDatabaseBroadcastReceiver);
	}

	/**********************************************************************************************
	 * Misc Methods
	 **********************************************************************************************/
	private void downloadDatabase()
	{
		// Binding com.openbox.realcomm.service is async, follow up in onServiceConnected
		bindDownloadDatabaseRecever();
		bindWebService();
	}

	private void updateUpdateMenuItemVisibility()
	{
		if (this.updateMenuItem != null)
		{
			if (RealCommApplication.updateNeeded)
			{
				this.updateMenuItem.setIcon(R.drawable.ic_menu_refresh_colour);
			}
			else
			{
				this.updateMenuItem.setIcon(R.drawable.ic_menu_refresh_grey);
			}
		}
	}

	protected void setDisplayHomeAsUpEnable(Boolean enabled)
	{
		if (this.actionBar != null)
		{
			this.actionBar.setDisplayHomeAsUpEnabled(enabled);
		}
	}

	private void startSettingsActivity()
	{
		Intent intent = new Intent(this, SettingsPageActivity.class);

		// Don't restart if already showing settings
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	private void startDataCaptureActivity()
	{
		Intent intent = new Intent(this, DataCaptureActivity.class);

		// Don't restart if already showing settings
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	public void hideSoftKeyboard()
	{
		InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}
}
