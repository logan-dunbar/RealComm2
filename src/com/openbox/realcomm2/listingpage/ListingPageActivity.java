package com.openbox.realcomm2.listingpage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.application.RealCommApplication;
import com.openbox.realcomm2.base.BaseActivity;
import com.openbox.realcomm2.database.models.BoothModel;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.database.models.TalkDayModel;
import com.openbox.realcomm2.database.models.VenueModel;
import com.openbox.realcomm2.utilities.enums.AppMode;
import com.openbox.realcomm2.utilities.enums.BeaconStatus;
import com.openbox.realcomm2.utilities.enums.BoothDataFragmentStatus;
import com.openbox.realcomm2.utilities.enums.BoothSortMode;
import com.openbox.realcomm2.utilities.enums.ProximityRegion;
import com.openbox.realcomm2.utilities.helpers.ClearFocusTouchListener;
import com.openbox.realcomm2.utilities.helpers.LogHelper;
import com.openbox.realcomm2.utilities.helpers.ToastHelper;
import com.openbox.realcomm2.utilities.interfaces.AppModeChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.AppModeInterface;
import com.openbox.realcomm2.utilities.interfaces.BeaconManagerBoundCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BeaconStatusChangeCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BoothDataInterface;
import com.openbox.realcomm2.utilities.interfaces.BoothDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BoothListInterface;
import com.openbox.realcomm2.utilities.interfaces.ClearFocusInterface;
import com.openbox.realcomm2.utilities.interfaces.ListingPageCallbacks;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.ScheduleDataInterface;
import com.openbox.realcomm2.utilities.interfaces.TimerTaskCallbacks;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

public class ListingPageActivity extends BaseActivity implements
	TimerTaskCallbacks,
	BoothDataChangedCallbacks,
	BoothDataInterface,
	ListingPageCallbacks,
	IBeaconConsumer,
	RangeNotifier,
	BeaconStatusChangeCallbacks,
	ClearFocusInterface,
	ScheduleDataChangedCallbacks,
	ScheduleDataInterface,
	AppModeChangedCallbacks,
	AppModeInterface,
	AppModeOfflineInterface
{
	private static final int ENABLE_BLUETOOTH_REQUEST = 1;

	private static final int TIMER_DELAY = 0 * 1000;
	private static final int TIMER_PERIOD = 2 * 1000;

	private static final String BOOTH_DATA_FRAGMENT_TAG = "boothData";
	private static final String SCHEDULE_DATA_FRAGMENT_TAG = "scheduleData";
	private static final Region ALL_BEACONS = new Region("regionId", null, null, null);

	private static final int NUMBER_OF_DISPLAY_BOOTHS = 3;
	private static final int NUMBER_OF_BIG_BOOTHS = 2;

	private static final String BEACON_STATUS_KEY = "beaconStatusKey";
	private static final String APP_MODE_KEY = "appModeKey";
	private static final String APP_MODE_SELECTOR_KEY = "appModeSelectorKey";

	/**********************************************************************************************
	 * Fields
	 **********************************************************************************************/
	// App Mode
	private AppModeManager appModeManager;

	// Beacon Manager
	private IBeaconManager beaconManager;

	// Listeners
	private BoothDataInterface boothDataListener;
	private List<BoothDataChangedCallbacks> boothDataChangedListeners = new ArrayList<>();
	private BoothListInterface boothListListener;
	private BeaconManagerBoundCallbacks beaconManagerBoundListener;

	private ScheduleDataInterface scheduleDataListener;
	private List<ScheduleDataChangedCallbacks> scheduleDataChangedListeners = new ArrayList<>();

	private List<AppModeChangedCallbacks> appModeChangedListeners = new ArrayList<>();

	// View State
	private List<Integer> boothIdsToDisplay = new ArrayList<Integer>();

	// ProgressBars
	// TODO might remove
	private ProgressBar topLeftProgressBar;
	private ProgressBar bottomLeftProgressBar;
	private ProgressBar bottomMiddleProgressBar;

	// TODO: will be removed
	private TextView accuracy1;
	private TextView accuracy2;
	private TextView accuracy3;
	private TextView appModeTextView;

	private Timer viewUpdateTimer;

	/**********************************************************************************************
	 * Activity Lifecycle Implements
	 **********************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listing_page_activity);

		// Initialize the App Mode and Beacon Status Managers
		initStateManagers(savedInstanceState);

		// Create the Booth Data Fragment
		createBoothDataFragment();

		// Create the List Fragment
		createBoothListFragment();

		// Create the Schedule Data Fragment
		createScheduleDataFragment();

		// Create the Dashboard Fragment
		createDashboardFragment();

		// Initialize the View
		initView();
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		registerBluetoothReceiver();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// Decide which mode app is currently in
		determineAppMode();

		// Kick view timer off
		startViewTimer();

	}

	@Override
	protected void onPause()
	{
		super.onPause();

		// Timers don't follow activity lifecycle, must manage ourselves
		stopViewTimer();

		// Pause the app
		this.appModeManager.changeAppMode(AppMode.Paused);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		// Save the current state of the system
		outState.putSerializable(BEACON_STATUS_KEY, this.appModeManager.getCurrentBeaconStatus());
		outState.putSerializable(APP_MODE_KEY, this.appModeManager.getCurrentAppMode());
		outState.putSerializable(APP_MODE_SELECTOR_KEY, this.appModeManager.getCurrentAppModeSelector());
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		unregisterBluetoothReceiver();

		// Make sure to free up unneeded resources
		this.appModeManager.unbindBeaconManager();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		// Clean up
		this.boothDataChangedListeners = null;
		this.boothDataListener = null;
		this.boothListListener = null;
		this.beaconManagerBoundListener = null;
		this.scheduleDataChangedListeners = null;
		this.scheduleDataListener = null;
		this.appModeChangedListeners = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ENABLE_BLUETOOTH_REQUEST)
		{
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK)
			{
				this.appModeManager.changeAppMode(AppMode.Online);
			}
			else
			{
				ToastHelper.showLongMessage(this, "Enable Bluetooth for Proximity Scanning");
				this.appModeManager.changeAppMode(AppMode.Offline);
			}
		}
	}

	@Override
	public void onAttachFragment(Fragment fragment)
	{
		super.onAttachFragment(fragment);

		// Attach the fragment listeners
		if (fragment instanceof BoothDataChangedCallbacks)
		{
			this.boothDataChangedListeners.add((BoothDataChangedCallbacks) fragment);
		}

		if (fragment instanceof BoothDataInterface)
		{
			this.boothDataListener = (BoothDataInterface) fragment;
		}

		if (fragment instanceof BoothListInterface)
		{
			this.boothListListener = (BoothListInterface) fragment;
		}

		if (fragment instanceof ScheduleDataInterface)
		{
			this.scheduleDataListener = (ScheduleDataInterface) fragment;
		}

		if (fragment instanceof ScheduleDataChangedCallbacks)
		{
			this.scheduleDataChangedListeners.add((ScheduleDataChangedCallbacks) fragment);
		}

		if (fragment instanceof AppModeChangedCallbacks)
		{
			this.appModeChangedListeners.add((AppModeChangedCallbacks) fragment);
		}
	}

	/**********************************************************************************************
	 * Bluetooth Receiver Methods
	 **********************************************************************************************/
	private void registerBluetoothReceiver()
	{
		if (RealCommApplication.getHasBluetoothLe())
		{
			final IntentFilter filter = new IntentFilter();
			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			registerReceiver(this.bluetoothReceiver, filter);
		}
	}

	private void unregisterBluetoothReceiver()
	{
		if (RealCommApplication.getHasBluetoothLe())
		{
			unregisterReceiver(this.bluetoothReceiver);
		}
	}

	/**********************************************************************************************
	 * Broadcast Receiver Implements
	 **********************************************************************************************/
	@Override
	public void onDownloadDatabaseReceive(Intent intent)
	{
		if (this.boothDataListener != null)
		{
			this.boothDataListener.refreshData();
		}

		if (this.scheduleDataListener != null)
		{
			this.scheduleDataListener.refreshData();
		}

		// Clean up
		super.onDownloadDatabaseReceive(intent);
	}

	/**********************************************************************************************
	 * Init Methods
	 **********************************************************************************************/
	private void initStateManagers(Bundle savedInstanceState)
	{
		BeaconStatus startingBeaconStatus = BeaconStatus.NonExistent;
		AppMode startingAppMode = AppMode.Initializing;
		AppMode startingAppModeSelector = AppMode.Online;

		if (savedInstanceState != null)
		{
			startingBeaconStatus = (BeaconStatus) savedInstanceState.getSerializable(BEACON_STATUS_KEY);
			startingAppMode = (AppMode) savedInstanceState.getSerializable(APP_MODE_KEY);
			startingAppModeSelector = (AppMode) savedInstanceState.getSerializable(APP_MODE_SELECTOR_KEY);
		}

		BeaconStatusManager beaconStatusManager = new BeaconStatusManager(this, startingBeaconStatus, RealCommApplication.getHasBluetoothLe());
		this.appModeManager = new AppModeManager(startingAppMode, startingAppModeSelector, beaconStatusManager, this, this);
	}

	private void createBoothDataFragment()
	{
		BoothDataFragment fragment = (BoothDataFragment) getSupportFragmentManager().findFragmentByTag(BOOTH_DATA_FRAGMENT_TAG);
		if (fragment == null)
		{
			getSupportFragmentManager().beginTransaction().add(BoothDataFragment.newInstance(), BOOTH_DATA_FRAGMENT_TAG).commit();
		}
	}

	private void createBoothListFragment()
	{
		BoothListFragment fragment = (BoothListFragment) getSupportFragmentManager().findFragmentById(R.id.boothListFragmentLayout);
		if (fragment == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.boothListFragmentLayout, BoothListFragment.newInstance()).commit();
		}
	}

	private void createScheduleDataFragment()
	{
		ScheduleDataFragment fragment = (ScheduleDataFragment) getSupportFragmentManager().findFragmentByTag(SCHEDULE_DATA_FRAGMENT_TAG);
		if (fragment == null)
		{
			getSupportFragmentManager().beginTransaction().add(ScheduleDataFragment.newInstance(), SCHEDULE_DATA_FRAGMENT_TAG).commit();
		}
	}

	private void createDashboardFragment()
	{
		DashboardFragment fragment = (DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.dashboardFragmentContainer);
		if (fragment == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.dashboardFragmentContainer, DashboardFragment.newInstance()).commit();
		}
	}

	private void initView()
	{
		// Register the clear focus touch listener
		LinearLayout listingPageLayout = (LinearLayout) findViewById(R.id.listingPageLayout);
		listingPageLayout.setOnTouchListener(new ClearFocusTouchListener(this, this));

		this.topLeftProgressBar = (ProgressBar) findViewById(R.id.topLeftProgressBar);
		this.bottomLeftProgressBar = (ProgressBar) findViewById(R.id.bottomLeftProgressBar);
		this.bottomMiddleProgressBar = (ProgressBar) findViewById(R.id.bottomMiddleProgressBar);

		// TODO: will be removed
		this.accuracy1 = (TextView) findViewById(R.id.accuracy1);
		this.accuracy2 = (TextView) findViewById(R.id.accuracy2);
		this.accuracy3 = (TextView) findViewById(R.id.accuracy3);
		this.appModeTextView = (TextView) findViewById(R.id.appModeTextView);

		View circle = findViewById(R.id.testCircle);
		GradientDrawable circleBackground = (GradientDrawable) circle.getBackground();
		circleBackground.setColor(getResources().getColor(R.color.opaque_orange));

		Button stopRangingButton = (Button) findViewById(R.id.stopRangingButton);
		Button startRangingButton = (Button) findViewById(R.id.startRangingButton);

		Button toggleSortModeButton = (Button) findViewById(R.id.toggleSortModeButton);

		OnClickListener clickListener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (v.getId() == R.id.stopRangingButton)
				{
					ListingPageActivity.this.appModeManager.changeAppMode(AppMode.Paused);
				}
				else if (v.getId() == R.id.startRangingButton)
				{
					ListingPageActivity.this.appModeManager.changeAppMode(ListingPageActivity.this.appModeManager.getCurrentAppModeSelector());
				}
				else if (v.getId() == R.id.toggleSortModeButton)
				{
					ListingPageActivity.this.toggleSortMode();
				}
			}
		};

		stopRangingButton.setOnClickListener(clickListener);
		startRangingButton.setOnClickListener(clickListener);
		toggleSortModeButton.setOnClickListener(clickListener);
		// END to be removed
	}

	// TODO: Must be updated properly
	private void toggleSortMode()
	{
		if (this.boothListListener != null)
		{
			this.boothListListener.toggleSortMode();
		}
	}

	/**********************************************************************************************
	 * Update View Methods
	 **********************************************************************************************/
	private void updateView()
	{
		switch (this.appModeManager.getCurrentAppMode())
		{
			case Initializing:
				// Do nothing
				break;
			case Offline:
				updateViewOffline();
				break;
			case Online:
				updateViewOnline();
				break;
			case OutOfRange:
				updateViewOutOfRange();
				break;
			case Paused:
				// Do nothing
			default:
				break;
		}
	}

	private void updateViewOffline()
	{
		if (this.boothDataListener != null && this.boothDataListener.getDataFragmentStatus() == BoothDataFragmentStatus.BoothsAndContactsLoaded)
		{
			this.boothIdsToDisplay = this.boothDataListener.getRandomBoothIds(NUMBER_OF_DISPLAY_BOOTHS);
		}

		updateBoothView();
	}

	private void updateViewOnline()
	{
		if (this.boothListListener != null && this.boothDataListener.getDataFragmentStatus() == BoothDataFragmentStatus.BoothsAndContactsLoaded)
		{
			this.boothListListener.updateList();
		}

		if (this.boothDataListener != null && this.boothDataListener.getDataFragmentStatus() == BoothDataFragmentStatus.BoothsAndContactsLoaded)
		{
			this.boothIdsToDisplay = this.boothDataListener.getClosestBoothIds(NUMBER_OF_DISPLAY_BOOTHS);
		}

		updateBoothView();
	}

	private void updateViewOutOfRange()
	{
		LogHelper.Log("updateViewOutOfRange");
		// TODO Must still implement
	}

	// This method does the actual updating of the fragments i.e. doing the fragment switch on ALL of the booths
	private void updateBoothView()
	{
		// TODO: Will be removed
		if (this.boothDataListener != null)
		{
			this.appModeTextView.setText(this.boothDataListener.getDataFragmentStatus().toString());
		}

		for (int i = 0; i < this.boothIdsToDisplay.size(); i++)
		{
			// TODO: Will be removed
			if (this.appModeManager.getCurrentAppMode() == AppMode.Online && this.boothDataListener != null)
			{
				getAccuracyTextView(i).setText(new DecimalFormat("#.##").format(this.boothDataListener.getBooth(i).getAccuracy()));
			}

			// Hide the progress bars
			// TODO: Might remove
			if (getProgressBar(i).getVisibility() != View.GONE)
			{
				getProgressBar(i).setVisibility(View.GONE);
			}

			// This is where the method starts (for reals)
			int newBoothId = this.boothIdsToDisplay.get(i);
			int containerId = getContainerId(i);

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			// TODO pimp out the animations
			// ft.setCustomAnimations(R.anim.flip_down_in, R.anim.flip_down_out);
			BoothFragment fragment = (BoothFragment) getSupportFragmentManager().findFragmentById(containerId);
			Boolean switchFragment = true;
			if (fragment != null)
			{
				if (fragment.getBoothId() == newBoothId)
				{
					fragment.updateBooth();
					switchFragment = false;
				}
				else
				{
					ft.remove(fragment);
				}
			}

			// If first fragment, or changing booths, then add
			if (switchFragment)
			{
				Boolean isBig = i < NUMBER_OF_BIG_BOOTHS;
				ft.add(containerId, BoothFragment.newInstance(newBoothId, isBig));
			}

			ft.commit();
		}
	}

	/**********************************************************************************************
	 * View Timer Methods
	 **********************************************************************************************/
	private void startViewTimer()
	{
		if (this.viewUpdateTimer == null)
		{
			this.viewUpdateTimer = new Timer();
			this.viewUpdateTimer.schedule(new ViewTimerTask(this), TIMER_DELAY, TIMER_PERIOD);
		}
	}

	private void stopViewTimer()
	{
		if (this.viewUpdateTimer != null)
		{
			this.viewUpdateTimer.cancel();
			this.viewUpdateTimer = null;
		}
	}

	/**********************************************************************************************
	 * Private Methods
	 **********************************************************************************************/
	private void determineAppMode()
	{
		if (this.appModeManager.getCurrentAppModeSelector() == AppMode.Online)
		{
			if (RealCommApplication.getHasBluetoothLe())
			{
				if (RealCommApplication.isBluetoothEnabled(this))
				{
					this.appModeManager.changeAppMode(AppMode.Online);
				}
				else
				{
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST);
				}
			}
			else
			{
				this.appModeManager.changeAppMode(AppMode.Offline);
			}
		}
		else
		{
			this.appModeManager.changeAppMode(AppMode.Offline);
		}
	}

	private ProgressBar getProgressBar(int index)
	{
		switch (index)
		{
			case 0:
				return this.topLeftProgressBar;
			case 1:
				return this.bottomLeftProgressBar;
			case 2:
				return this.bottomMiddleProgressBar;
			default:
				return null;
		}
	}

	private TextView getAccuracyTextView(int index)
	{
		switch (index)
		{
			case 0:
				return this.accuracy1;
			case 1:
				return this.accuracy2;
			case 2:
				return this.accuracy3;
			default:
				return null;
		}
	}

	private int getContainerId(int index)
	{
		switch (index)
		{
			case 0:
				return R.id.topLeftLayout;
			case 1:
				return R.id.bottomLeftLayout;
			case 2:
				return R.id.bottomMiddleLayout;
			default:
				return 0;
		}
	}

	/**********************************************************************************************
	 * ALL THE IMPLEMENTS
	 **********************************************************************************************/
	/*
	 * View Timer
	 */
	@Override
	public void onTimerTick()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				ListingPageActivity.this.updateView();
			}
		});
	}

	/*
	 * Booth Data Loaded
	 */
	@Override
	public void onBoothDataLoaded()
	{
		for (BoothDataChangedCallbacks listener : this.boothDataChangedListeners)
		{
			listener.onBoothDataLoaded();
		}
	}

	@Override
	public void onCompanyDataLoaded()
	{
		for (BoothDataChangedCallbacks listener : this.boothDataChangedListeners)
		{
			listener.onCompanyDataLoaded();
		}

		// TODO Do stuff
		// LogHelper.Log("onCompaniesLoaded");
		// updateBoothListFragment();
	}

	@Override
	public void onBoothDataUpdated()
	{
		for (BoothDataChangedCallbacks listener : this.boothDataChangedListeners)
		{
			listener.onBoothDataUpdated();
		}
	}

	/*
	 * Booth Data Access
	 */
	@Override
	public BoothDataFragmentStatus getDataFragmentStatus()
	{
		if (this.boothDataListener != null)
		{
			return this.boothDataListener.getDataFragmentStatus();
		}

		return null;
	}

	@Override
	public void refreshData()
	{
		// if (this.boothDataListener != null)
		// {
		// this.boothDataListener.refreshData();
		// }
	}

	@Override
	public BoothViewModel getBoothViewModel(int boothId)
	{
		if (this.boothDataListener != null && this.boothDataListener.getDataFragmentStatus() == BoothDataFragmentStatus.BoothsAndContactsLoaded)
		{
			return this.boothDataListener.getBoothViewModel(boothId);
		}

		return null;
	}

	@Override
	public List<BoothViewModel> getBooothViewModels()
	{
		// TODO: maybe limit it to always getting by sort mode
		return null;
	}

	@Override
	public List<BoothViewModel> getBoothViewModels(BoothSortMode boothSortMode)
	{
		if (this.boothDataListener != null && this.boothDataListener.getDataFragmentStatus() == BoothDataFragmentStatus.BoothsAndContactsLoaded)
		{
			return this.boothDataListener.getBoothViewModels(boothSortMode);
		}

		return null;
	}

	@Override
	public void updateBoothDistances(Collection<IBeacon> beaconList)
	{
	}

	@Override
	public void resetBoothDistances()
	{
	}

	@Override
	public List<Integer> getClosestBoothIds(int numberOfDisplayBooths)
	{
		return null;
	}

	@Override
	public List<Integer> getRandomBoothIds(int numberOfDisplayBooths)
	{
		return null;
	}

	@Override
	public Map<ProximityRegion, Integer> getBoothProximityCounts()
	{
		if (this.boothDataListener != null)
		{
			return this.boothDataListener.getBoothProximityCounts();
		}

		return null;
	}

	/*
	 * App Mode
	 */
	@Override
	public void onAppModeChanged()
	{
		for (AppModeChangedCallbacks appModeChangedListener : this.appModeChangedListeners)
		{
			appModeChangedListener.onAppModeChanged();
		}
	}

	@Override
	public void onAppModeOffline()
	{
		if (this.boothDataListener != null)
		{
			this.boothDataListener.resetBoothDistances();
		}
	}

	@Override
	public AppMode getCurrentAppMode()
	{
		if (this.appModeManager != null)
		{
			return this.appModeManager.getCurrentAppMode();
		}

		return null;
	}

	/*
	 * Listing Page
	 */
	@Override
	public void goToProfilePage(int companyId)
	{
		ToastHelper.showShortMessage(this, "Going to companyId=" + companyId);
	}

	/**
	 * Beacon Consumer
	 */
	@Override
	public void onIBeaconServiceConnect()
	{
		this.beaconManager.setRangeNotifier(this);

		if (this.beaconManagerBoundListener != null)
		{
			this.beaconManagerBoundListener.onBeaconManagerBound();
			this.beaconManagerBoundListener = null;
		}
	}

	/**
	 * Range Notifier
	 */
	@Override
	public void didRangeBeaconsInRegion(final Collection<IBeacon> beaconList, Region region)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (ListingPageActivity.this.boothDataListener != null &&
					(ListingPageActivity.this.boothDataListener.getDataFragmentStatus() == BoothDataFragmentStatus.BoothsAndContactsLoaded ||
					ListingPageActivity.this.boothDataListener.getDataFragmentStatus() == BoothDataFragmentStatus.BoothsLoaded))
				{
					ListingPageActivity.this.boothDataListener.updateBoothDistances(beaconList);
				}
			}
		});
	}

	/**
	 * Beacon Status Change
	 */
	@Override
	public void initializeBeaconManager()
	{
		this.beaconManager = IBeaconManager.getInstanceForApplication(this);
	}

	@Override
	public void bindBeaconManager()
	{
		this.beaconManager.bind(this);
	}

	@Override
	public void bindBeaconManagerAndRange(BeaconManagerBoundCallbacks listener)
	{
		this.beaconManagerBoundListener = listener;
		this.beaconManager.bind(this);
	}

	@Override
	public void unbindBeaconManager()
	{
		this.beaconManager.unBind(this);
	}

	@Override
	public void startRangingBeacons()
	{
		try
		{
			this.beaconManager.startRangingBeaconsInRegion(ALL_BEACONS);
		}
		catch (RemoteException e)
		{
			LogHelper.Log("Can't range beacons: " + e);
		}
	}

	@Override
	public void stopRangingBeacons()
	{
		try
		{
			this.beaconManager.stopRangingBeaconsInRegion(ALL_BEACONS);
		}
		catch (RemoteException e)
		{
			LogHelper.Log("Can't stop ranging beacons: " + e);
		}
	}

	/**
	 * Schedule Data Loaded
	 */
	@Override
	public void onScheduleDataLoaded()
	{
		for (ScheduleDataChangedCallbacks listener : this.scheduleDataChangedListeners)
		{
			listener.onScheduleDataLoaded();
		}
	}

	/**
	 * Schedule Data Access
	 */
	@Override
	public TreeMap<Date, String> getDistinctDayList()
	{
		if (this.scheduleDataListener != null)
		{
			return this.scheduleDataListener.getDistinctDayList();
		}

		return null;
	}

	@Override
	public List<TalkDayModel> getTalkDayList()
	{
		if (this.scheduleDataListener != null)
		{
			return this.scheduleDataListener.getTalkDayList();
		}

		return null;
	}

	@Override
	public TalkDayModel getTalkDay(Date talkDate)
	{
		if (this.scheduleDataListener != null)
		{
			return this.scheduleDataListener.getTalkDay(talkDate);
		}

		return null;
	}

	@Override
	public VenueModel getVenueForDate(Date talkDate, int venueId)
	{
		if (this.scheduleDataListener != null)
		{
			return this.scheduleDataListener.getVenueForDate(talkDate, venueId);
		}

		return null;
	}

	/**********************************************************************************************
	 * Misc Methods and Classes
	 **********************************************************************************************/
	/**
	 * Broadcast receiver to receive bluetooth change events
	 */
	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
			{
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				switch (state)
				{
					case BluetoothAdapter.STATE_OFF:
						ToastHelper.showLongMessage(ListingPageActivity.this, "Offline mode engaged!");
						ListingPageActivity.this.appModeManager.changeAppMode(AppMode.Offline);
						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						// ToastHelper.showShortMessage(ListingPageActivity.this, "Bluetooth turning off!");
						break;
					case BluetoothAdapter.STATE_ON:
						ToastHelper.showLongMessage(ListingPageActivity.this, "Online mode engaged!");
						ListingPageActivity.this.appModeManager.changeAppMode(AppMode.Online);
						break;
					case BluetoothAdapter.STATE_TURNING_ON:
						// ToastHelper.showShortMessage(ListingPageActivity.this, "Bluetooth turning on!");
						break;
				}
			}
		}
	};

	@Override
	public BoothModel getBooth(int index)
	{
		if (this.boothDataListener != null)
		{
			return this.boothDataListener.getBooth(index);
		}

		return null;
	}

	@Override
	public View getViewToClearFocus()
	{
		if (this.boothListListener != null)
		{
			return this.boothListListener.getViewToClearFocus();
		}

		return null;
	}
}
