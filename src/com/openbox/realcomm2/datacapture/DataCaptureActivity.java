package com.openbox.realcomm2.datacapture;

import android.os.Bundle;

import com.openbox.realcomm2.application.RealCommApplication;
import com.openbox.realcomm2.base.BaseActivity;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.Region;

public class DataCaptureActivity extends BaseActivity
{
	private static final Region ALL_BEACONS = new Region("regionId", null, null, null);
	
	private IBeaconManager beaconManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		if (RealCommApplication.getHasBluetoothLe())
		{
			this.beaconManager = IBeaconManager.getInstanceForApplication(this);
		}
	}
}
