package com.openbox.realcomm2.application;


import com.openbox.realcomm2.database.DatabaseManager;
import com.openbox.realcomm2.listingpage.ListingPageActivity;
import com.openbox.realcomm2.services.WebService;
import com.openbox.realcomm2.settingspage.SettingsPageActivity;
import com.openbox.realcomm2.utilities.helpers.LogHelper;
import com.openbox.realcomm2.utilities.menu.RealCommMenuItem;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.FragmentManager;

public class RealCommApplication extends Application
{
	// ONLY STATIC VARIABLES/METHODS IN HERE. THIS IS BOUND TO THE CONTEXT
	// Note: If the app goes to background and is closed by the OS, this object is not persisted. Use at own risk

	public static Boolean updateNeeded = false;
	public static Boolean downloadAndWriteDatabaseSucceeded = false;
	private static Boolean hasBluetoothLE = false;
	
	private Typeface exo2Font;
	private Typeface exo2FontBold;
	private Typeface nunitoFont;
	private Typeface nunitoFontBold;

	public static Boolean getHasBluetoothLe()
	{
		return hasBluetoothLE;
	}
	
	public Typeface getExo2Font()
	{
		return this.exo2Font;
	}
	
	public Typeface getExo2FontBold()
	{
		return exo2FontBold;
	}

	public Typeface getNunitoFont()
	{
		return this.nunitoFont;
	}

	public Typeface getNunitoFontBold()
	{
		return nunitoFontBold;
	}

	public static final RealCommMenuItem[] menuItems =
	{
		new RealCommMenuItem("Listing", ListingPageActivity.class),
		new RealCommMenuItem("Settings", SettingsPageActivity.class)
	};

	@Override
	public void onCreate()
	{
		super.onCreate();

		this.exo2Font = Typeface.createFromAsset(getAssets(), "fonts/Exo_2/Exo2-Regular.ttf");
		this.exo2FontBold = Typeface.createFromAsset(getAssets(), "fonts/Exo_2/Exo2-Bold.ttf");
		
		this.nunitoFont = Typeface.createFromAsset(getAssets(), "fonts/Nunito/Nunito-Regular.ttf");
		this.nunitoFontBold = Typeface.createFromAsset(getAssets(), "fonts/Nunito/Nunito-Bold.ttf");
		
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
		{
			hasBluetoothLE = true;
		}

		LogHelper.Log("Has Proximity Capabilities: " + hasBluetoothLE);

		// Initialize the DBManager
		DatabaseManager.init(getApplicationContext());

		// Start WebService
		Intent service = new Intent(getApplicationContext(), WebService.class);
		startService(service);
	}
	
	public static Boolean isBluetoothEnabled(Context context)
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (bluetoothAdapter == null)
		{
			return false;
		}
		else
		{
			return bluetoothAdapter.isEnabled();
		}
	}
}
