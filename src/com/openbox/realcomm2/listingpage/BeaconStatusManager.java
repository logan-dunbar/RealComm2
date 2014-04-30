package com.openbox.realcomm2.listingpage;

import com.openbox.realcomm2.utilities.enums.BeaconStatus;
import com.openbox.realcomm2.utilities.interfaces.BeaconManagerBoundCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BeaconStatusChangeCallbacks;

public class BeaconStatusManager implements BeaconManagerBoundCallbacks
{
	private BeaconStatusChangeCallbacks listener;

	private BeaconStatus currentBeaconStatus;
	private BeaconStatus tempNewBeaconStatus;
	private Boolean hasBluetoothLe;

	public BeaconStatus getCurrentBeaconStatus()
	{
		return this.currentBeaconStatus;
	}

	public void setCurrentBeaconStatus(BeaconStatus currentBeaconStatus)
	{
		this.currentBeaconStatus = currentBeaconStatus;
	}

	public BeaconStatusManager(BeaconStatusChangeCallbacks listener, BeaconStatus startingBeaconStatus, Boolean hasBluetoothLe)
	{
		this.listener = listener;
		this.currentBeaconStatus = startingBeaconStatus;
		this.hasBluetoothLe = hasBluetoothLe;
	}

	public void changeBeaconStatus(BeaconStatus newBeaconStatus)
	{
		if (!this.hasBluetoothLe)
		{
			// No beacons needed if no BLE
			return;
		}

		switch (newBeaconStatus)
		{
			case NonExistent:
				// Should not get here
				break;
			case Unbound:
				changeBeaconStatusUnbound(newBeaconStatus);
				break;
			case Bound:
				changeBeaconStatusBound(newBeaconStatus);
				break;
			case Ranging:
				changeBeaconStatusRanging(newBeaconStatus);
				break;
			default:
				break;
		}
	}

	private void changeBeaconStatusUnbound(BeaconStatus newBeaconStatus)
	{
		switch (this.currentBeaconStatus)
		{
			case NonExistent:
				changeNonExistentToUnbound();
				changeBeaconStatus(newBeaconStatus);
				break;
			case Unbound:
				// Stay here
				break;
			case Bound:
				changeBoundToUnbound();
				changeBeaconStatus(newBeaconStatus);
				break;
			case Ranging:
				changeRangingToBound();
				changeBeaconStatus(newBeaconStatus);
				break;
			default:
				break;
		}
	}

	private void changeBeaconStatusBound(BeaconStatus newBeaconStatus)
	{
		switch (this.currentBeaconStatus)
		{
			case NonExistent:
				changeNonExistentToUnbound();
				changeBeaconStatus(newBeaconStatus);
				break;
			case Unbound:
				changeUnboundToBound();
				changeBeaconStatus(newBeaconStatus);
				break;
			case Bound:
				// Stay here
				break;
			case Ranging:
				changeRangingToBound();
				changeBeaconStatus(newBeaconStatus);
				break;
			default:
				break;
		}
	}

	private void changeBeaconStatusRanging(BeaconStatus newBeaconStatus)
	{
		switch (this.currentBeaconStatus)
		{
			case NonExistent:
				changeNonExistentToUnbound();
				changeBeaconStatus(newBeaconStatus);
				break;
			case Unbound:
				changeUnboundToBoundAndRange();
				this.tempNewBeaconStatus = newBeaconStatus;
				break;
			case Bound:
				changeBoundToRanging();
				changeBeaconStatus(newBeaconStatus);
				break;
			case Ranging:
				// Stay here
				break;
			default:
				break;
		}
	}

	private void changeNonExistentToUnbound()
	{
		this.listener.initializeBeaconManager();
		this.currentBeaconStatus = BeaconStatus.Unbound;
	}

	private void changeUnboundToBound()
	{
		this.listener.bindBeaconManager();
		this.currentBeaconStatus = BeaconStatus.Bound;
	}

	private void changeUnboundToBoundAndRange()
	{
		this.listener.bindBeaconManagerAndRange(this);
		this.currentBeaconStatus = BeaconStatus.Bound;
	}

	@Override
	public void onBeaconManagerBound()
	{
		changeBeaconStatus(this.tempNewBeaconStatus);
		this.tempNewBeaconStatus = null;
	}

	private void changeBoundToUnbound()
	{
		this.listener.unbindBeaconManager();
		this.currentBeaconStatus = BeaconStatus.Unbound;
	}

	private void changeBoundToRanging()
	{
		this.listener.startRangingBeacons();
		this.currentBeaconStatus = BeaconStatus.Ranging;
	}

	private void changeRangingToBound()
	{
		this.listener.stopRangingBeacons();
		this.currentBeaconStatus = BeaconStatus.Bound;
	}
	// private void template()
	// {
	// switch (this.currentBeaconStatus)
	// {
	// case NonExistent:
	// break;
	// case Unbound:
	// break;
	// case Bound:
	// break;
	// case Ranging:
	// break;
	// default:
	// break;
	// }
	// }
}
