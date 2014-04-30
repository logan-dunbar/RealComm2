package com.openbox.realcomm2.application;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager
{
	/**********************************************************************************************
	 * Constants
	 **********************************************************************************************/
	public static final String WEB_SERVICE_PREFERENCES_NAME = "webServicePreferences";
	public static final String CONFIGURATION_PREFERENCES_NAME = "configurationPreferences";
	
	public static final String MOST_RECENT_UPDATE_DATE_MEMBER_NAME = "MostRecentUpdateDate";
	public static final String LAST_UPDATE_DATE_MEMBER_NAME = "LastUpdateDate";

	public static final String PROXIMITY_ENABLED_MEMBER_NAME = "ProximityEnabled";
	
	public static final Long DEFAULT_LONG_VALUE = Long.valueOf(-1);

	/**********************************************************************************************
	 * Fields
	 **********************************************************************************************/
	private static String mostRecentUpdateDate = null;
	private static Long lastUpdateDate = null;
	private static Boolean proximityEnabled = null;
	
	
	/**********************************************************************************************
	 * Hidden singleton constructor and shared preference getters
	 **********************************************************************************************/
	private SharedPreferencesManager() {}
	
	private static SharedPreferences getGeneralSharedPreferences(Context context) {
        return context.getSharedPreferences(WEB_SERVICE_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
	
	private static SharedPreferences getConfigurationSharedPreferences(Context context) {
        return context.getSharedPreferences(CONFIGURATION_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
	
	/**********************************************************************************************
	 * Web Service Preferences
	 **********************************************************************************************/
	public static String getMostRecentUpdateDate(Context context)
	{
		if (mostRecentUpdateDate == null)
		{
			mostRecentUpdateDate = getGeneralSharedPreferences(context).getString(MOST_RECENT_UPDATE_DATE_MEMBER_NAME, null);
		}
		
		return mostRecentUpdateDate;
	}
	
	public static void setMostRecentUpdateDate(Context context, String newDate)
	{
		mostRecentUpdateDate = newDate;
		final SharedPreferences.Editor editor = getGeneralSharedPreferences(context).edit();
		editor.putString(MOST_RECENT_UPDATE_DATE_MEMBER_NAME, mostRecentUpdateDate);
		editor.commit();
	}
	
	public static Long getLastUpdateDate(Context context)
	{
		if (lastUpdateDate == null)
		{
			lastUpdateDate = getGeneralSharedPreferences(context).getLong(LAST_UPDATE_DATE_MEMBER_NAME, DEFAULT_LONG_VALUE);
		}
		
		return lastUpdateDate;
	}
	
	public static void setLastUpdateDate(Context context, Long newDate)
	{
		lastUpdateDate = newDate;
		final SharedPreferences.Editor editor = getGeneralSharedPreferences(context).edit();
		editor.putLong(LAST_UPDATE_DATE_MEMBER_NAME, lastUpdateDate);
		editor.commit();
	}

	/**********************************************************************************************
	 * TODO Don't need these if we go with the preference page found in res/xml/preferences.xml
	 * Configuration Preferences
	 **********************************************************************************************/
	public static Boolean getProximityEnabled(Context context)
	{
		if (proximityEnabled == null)
		{
			proximityEnabled = getConfigurationSharedPreferences(context).getBoolean(PROXIMITY_ENABLED_MEMBER_NAME, true);
		}
		
		return proximityEnabled;
	}
	
	public static void setProximityEnabled(Context context, Boolean enabled)
	{
		proximityEnabled = enabled;
		final SharedPreferences.Editor editor = getGeneralSharedPreferences(context).edit();
		editor.putBoolean(PROXIMITY_ENABLED_MEMBER_NAME, proximityEnabled);
		
		// Apply is better when running on UI Thread, as it queues the calls
		editor.apply();
	}
}
