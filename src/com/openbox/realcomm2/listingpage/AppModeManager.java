package com.openbox.realcomm2.listingpage;

import com.openbox.realcomm2.utilities.enums.AppMode;
import com.openbox.realcomm2.utilities.enums.BeaconStatus;
import com.openbox.realcomm2.utilities.interfaces.AppModeChangedCallbacks;

public class AppModeManager
{
	private AppMode currentAppMode;
	private AppMode currentAppModeSelector;
	private BeaconStatusManager beaconStatusManager;
	private AppModeChangedCallbacks appModeChangedListener;
	private AppModeOfflineInterface appModeOfflineListener;

	public AppMode getCurrentAppMode()
	{
		return this.currentAppMode;
	}

	public void setCurrentAppMode(AppMode currentAppMode)
	{
		this.currentAppMode = currentAppMode;
	}

	public AppMode getCurrentAppModeSelector()
	{
		return this.currentAppModeSelector;
	}

	public void setCurrentAppModeSelector(AppMode currentAppModeSelector)
	{
		this.currentAppModeSelector = currentAppModeSelector;
	}

	public BeaconStatus getCurrentBeaconStatus()
	{
		return this.beaconStatusManager.getCurrentBeaconStatus();
	}

	public void setCurrentBeaconStatus(BeaconStatus currentBeaconStatus)
	{
		this.beaconStatusManager.setCurrentBeaconStatus(currentBeaconStatus);
	}

	public AppModeManager(AppMode startingAppMode, AppMode startingAppModeSelector, BeaconStatusManager beaconStatusManager,
		AppModeChangedCallbacks appModeListener, AppModeOfflineInterface appModeOfflineListener)
	{
		this.currentAppMode = startingAppMode;
		this.currentAppModeSelector = startingAppModeSelector;
		this.beaconStatusManager = beaconStatusManager;
		this.appModeChangedListener = appModeListener;
		this.appModeOfflineListener = appModeOfflineListener;
	}

	public void unbindBeaconManager()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Unbound);
	}

	public void changeAppMode(AppMode newAppMode)
	{
		switch (newAppMode)
		{
			case Initializing:
				// Should not get here
				break;
			case Offline:
				changeAppModeOffline(newAppMode);
				break;
			case Online:
				changeAppModeOnline(newAppMode);
				break;
			case OutOfRange:
				changeAppModeOutOfRange(newAppMode);
				break;
			case Paused:
				changeAppModePaused(newAppMode);
				break;
			default:
				break;
		}
	}

	private void changeAppModeOffline(AppMode newAppMode)
	{
		switch (this.currentAppMode)
		{
			case Initializing:
				changeInitializingToOffline();
				changeAppMode(newAppMode);
				break;
			case Offline:
				// Stay here
				break;
			case Online:
				changeOnlineToOffline();
				changeAppMode(newAppMode);
				break;
			case OutOfRange:
				changeOutOfRangeToOnline();
				changeAppMode(newAppMode);
				break;
			case Paused:
				changePausedToOffline();
				changeAppMode(newAppMode);
				break;
			default:
				break;
		}
	}

	private void changeAppModeOnline(AppMode newAppMode)
	{
		switch (this.currentAppMode)
		{
			case Initializing:
				changeInitializingToOnline();
				changeAppMode(newAppMode);
				break;
			case Offline:
				changeOfflineToOnline();
				changeAppMode(newAppMode);
				break;
			case Online:
				// Stay here
				break;
			case OutOfRange:
				changeOutOfRangeToOnline();
				changeAppMode(newAppMode);
				break;
			case Paused:
				changePausedToOnline();
				changeAppMode(newAppMode);
				break;
			default:
				break;
		}
	}

	private void changeAppModeOutOfRange(AppMode newAppMode)
	{
		switch (this.currentAppMode)
		{
			case Initializing:
				// Can't do this
				break;
			case Offline:
				// Can't do this
				break;
			case Online:
				changeOnlineToOutOfRange();
				changeAppMode(newAppMode);
				break;
			case OutOfRange:
				// Stay here
				break;
			case Paused:
				changePausedToOnline();
				changeAppMode(newAppMode);
				break;
			default:
				break;
		}
	}

	private void changeAppModePaused(AppMode newAppMode)
	{
		switch (this.currentAppMode)
		{
			case Initializing:
				// TODO: can't do this?
				break;
			case Offline:
				changeOfflineToPaused();
				changeAppMode(newAppMode);
				break;
			case Online:
				changeOnlineToPaused();
				changeAppMode(newAppMode);
				break;
			case OutOfRange:
				changeOutOfRangeToPaused();
				changeAppMode(newAppMode);
				break;
			case Paused:
				// Stay here
				break;
			default:
				break;
		}
	}

	private void changeInitializingToOffline()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Unbound);
		changeAppMode(AppMode.Offline, true);
	}

	private void changeInitializingToOnline()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Ranging);
		changeAppMode(AppMode.Online, true);
	}

	private void changeOfflineToOnline()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Ranging);
		changeAppMode(AppMode.Online, true);
	}

	private void changeOnlineToOffline()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Unbound);
		changeAppMode(AppMode.Offline, true);
		notifyAppModeOffline();
	}

	private void changeOfflineToPaused()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Bound);
		changeAppMode(AppMode.Paused, false);
	}

	private void changePausedToOffline()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Unbound);
		changeAppMode(AppMode.Offline, true);
		notifyAppModeOffline();
	}

	private void changeOnlineToPaused()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Bound);
		changeAppMode(AppMode.Paused, false);
	}

	private void changePausedToOnline()
	{
		this.beaconStatusManager.changeBeaconStatus(BeaconStatus.Ranging);
		changeAppMode(AppMode.Online, true);
	}

	private void changeOnlineToOutOfRange()
	{
		// TODO
		changeAppMode(AppMode.OutOfRange, false);
	}

	private void changeOutOfRangeToOnline()
	{
		// TODO
		changeAppMode(AppMode.Online, true);
	}

	private void changeOutOfRangeToPaused()
	{
		// TODO
		changeAppMode(AppMode.Paused, false);
	}

	private void changeAppMode(AppMode appMode, Boolean updateAppModeSelector)
	{
		this.currentAppMode = appMode;

		if (updateAppModeSelector)
		{
			this.currentAppModeSelector = appMode;
		}

		if (this.appModeChangedListener != null)
		{
			this.appModeChangedListener.onAppModeChanged();
		}
	}
	
	private void notifyAppModeOffline()
	{
		// Doesn't happen from Initializing -> Offline (no need)
		
		// TODO This should probably really be done a different way, but its the end of the project,
		// and I'm tired. Maybe if there is time.
		if (this.appModeOfflineListener != null)
		{
			this.appModeOfflineListener.onAppModeOffline();
		}
	}

	// private void template()
	// {
	// switch (this.currentAppMode)
	// {
	// case Initializing:
	// break;
	// case Offline:
	// break;
	// case Online:
	// break;
	// case OutOfRange:
	// break;
	// case Paused:
	// break;
	// default:
	// break;
	// }
	// }
}
