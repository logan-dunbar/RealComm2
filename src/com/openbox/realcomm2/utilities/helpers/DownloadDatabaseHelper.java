package com.openbox.realcomm2.utilities.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.openbox.realcomm2.application.SharedPreferencesManager;
import com.openbox.realcomm2.database.DatabaseManager;
import com.openbox.realcomm2.database.objects.Booth;
import com.openbox.realcomm2.database.objects.BoothContact;
import com.openbox.realcomm2.database.objects.Company;
import com.openbox.realcomm2.database.objects.CompanyLogo;
import com.openbox.realcomm2.database.objects.Contact;
import com.openbox.realcomm2.database.objects.ContactImage;
import com.openbox.realcomm2.database.objects.RealCommDatabase;
import com.openbox.realcomm2.database.objects.Talk;
import com.openbox.realcomm2.database.objects.TalkTrack;
import com.openbox.realcomm2.database.objects.Venue;

public class DownloadDatabaseHelper
{
	/**********************************************************************************************
	 * Members
	 **********************************************************************************************/
	private static String serverMostRecentUpdateDate;

	private Context context;
	private String urlString;
	private Gson gson;
	private JsonParser jsonParser;

	private Boolean checkUpdateSucceeded = false;;
	private Boolean updateNeeded = false;
	private Boolean downloadDatabaseSucceeded = false;
	private Boolean writeDatabaseSucceeded = false;

	public Boolean getCheckUpdateSucceeded()
	{
		return this.checkUpdateSucceeded;
	}

	public Boolean getUpdateNeeded()
	{
		return this.updateNeeded;
	}

	public Boolean getDownloadDatabaseSucceeded()
	{
		return this.downloadDatabaseSucceeded;
	}

	public Boolean getWriteDatabaseSucceeded()
	{
		return this.writeDatabaseSucceeded;
	}

	private RealCommDatabase realCommDatabase;

	/**********************************************************************************************
	 * Constructor
	 **********************************************************************************************/
	public DownloadDatabaseHelper(Context context, String urlString)
	{
		this.context = context;
		this.urlString = urlString;
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		this.jsonParser = new JsonParser();
	}

	/**********************************************************************************************
	 * Public Methods
	 **********************************************************************************************/
	public void checkUpdateNeeded()
	{
		LogHelper.Log("Checking if needs to update...");
		URL url;
		try
		{
			// Set up the connection
			url = new URL(this.urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			// Specify request properties asking for JSON
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			// Get the input stream
			InputStream stream = urlConnection.getInputStream();

			// Read the data
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			this.updateNeeded = readMostRecentUpdateDateAndCompareToSaved(reader);
			this.checkUpdateSucceeded = true;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// UnknownHostException comes in here
			e.printStackTrace();
		}
	}

	public void downloadDatabase()
	{
		// TODO LD - NB Change - com.openbox.realcomm.database is downloaded and completely loaded into memory, and then
		// written, might need
		// to be incremental based on DB size
		LogHelper.Log("Downloading com.openbox.realcomm.database...");
		URL url;
		try
		{
			// Set up the connection
			url = new URL(this.urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			// Specify request properties asking for JSON
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			// Get the input stream
			InputStream stream = urlConnection.getInputStream();

			// Read the data
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			LogHelper.Log("Download complete!");

			readRealCommDatabaseJson(reader);
			this.downloadDatabaseSucceeded = true;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void writeDatabase()
	{
		DatabaseManager databaseManager = DatabaseManager.getInstance();

		try
		{
			databaseManager.deleteAll(Booth.class);
			databaseManager.deleteAll(BoothContact.class);
			databaseManager.deleteAll(Contact.class);
			databaseManager.deleteAll(ContactImage.class);
			databaseManager.deleteAll(Company.class);
			databaseManager.deleteAll(CompanyLogo.class);
			databaseManager.deleteAll(Talk.class);
			databaseManager.deleteAll(TalkTrack.class);
			databaseManager.deleteAll(Venue.class);

			databaseManager.createList(realCommDatabase.getBoothList(), Booth.class, Booth.ID_CLASS);
			databaseManager.createList(realCommDatabase.getBoothContactList(), BoothContact.class, BoothContact.ID_CLASS);
			databaseManager.createList(realCommDatabase.getContactList(), Contact.class, Contact.ID_CLASS);
			databaseManager.createList(realCommDatabase.getContactImageList(), ContactImage.class, ContactImage.ID_CLASS);
			databaseManager.createList(realCommDatabase.getCompanyList(), Company.class, Company.ID_CLASS);
			databaseManager.createList(realCommDatabase.getCompanyLogoList(), CompanyLogo.class, CompanyLogo.ID_CLASS);
			databaseManager.createList(realCommDatabase.getTalkList(), Talk.class, Talk.ID_CLASS);
			databaseManager.createList(realCommDatabase.getTalkTrackList(), TalkTrack.class, TalkTrack.ID_CLASS);
			databaseManager.createList(realCommDatabase.getVenueList(), Venue.class, Venue.ID_CLASS);

			SharedPreferencesManager.setMostRecentUpdateDate(this.context, serverMostRecentUpdateDate);
			SharedPreferencesManager.setLastUpdateDate(this.context, new Date().getTime());
			this.writeDatabaseSucceeded = true;
		}
		catch (Exception e)
		{
			// Something went wrong with the insert
			e.printStackTrace();
		}
	}

	/**********************************************************************************************
	 * Json Parsing Methods
	 **********************************************************************************************/
	private Boolean readMostRecentUpdateDateAndCompareToSaved(BufferedReader reader)
	{
		// Get the two dates
		JsonObject mostRecentUpdateDateObject = this.jsonParser.parse(reader).getAsJsonObject();
		serverMostRecentUpdateDate = mostRecentUpdateDateObject.get(SharedPreferencesManager.MOST_RECENT_UPDATE_DATE_MEMBER_NAME).getAsString();

		String localMostRecentUpdateDate = SharedPreferencesManager.getMostRecentUpdateDate(this.context);

		LogHelper.Log("Update Needed = " + !serverMostRecentUpdateDate.equals(localMostRecentUpdateDate));

		return !serverMostRecentUpdateDate.equals(localMostRecentUpdateDate);
	}

	private void readRealCommDatabaseJson(BufferedReader reader)
	{
		JsonObject realCommDatabaseJsonObject = this.jsonParser.parse(reader).getAsJsonObject();
		realCommDatabase = this.gson.fromJson(realCommDatabaseJsonObject, RealCommDatabase.class);
		LogHelper.Log("Conversion complete!");
	}
}
