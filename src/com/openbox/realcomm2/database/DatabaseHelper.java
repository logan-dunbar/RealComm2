package com.openbox.realcomm2.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.openbox.realcomm2.database.objects.BeaconInfo;
import com.openbox.realcomm2.database.objects.Booth;
import com.openbox.realcomm2.database.objects.BoothContact;
import com.openbox.realcomm2.database.objects.Company;
import com.openbox.realcomm2.database.objects.CompanyLogo;
import com.openbox.realcomm2.database.objects.Contact;
import com.openbox.realcomm2.database.objects.ContactImage;
import com.openbox.realcomm2.database.objects.Talk;
import com.openbox.realcomm2.database.objects.TalkTrack;
import com.openbox.realcomm2.database.objects.Venue;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	/**********************************************************************************************
	 * Database Configuration
	 **********************************************************************************************/
	private static final String DATABASE_NAME = "RealCommDB.sqlite";
	private static final int DATABASE_VERSION = 2;

	/**********************************************************************************************
	 * Constructor
	 **********************************************************************************************/
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**********************************************************************************************
	 * onCreate and onUpgrade Events
	 **********************************************************************************************/
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTable(connectionSource, Booth.class);
			TableUtils.createTable(connectionSource, BoothContact.class);
			TableUtils.createTable(connectionSource, Contact.class);
			TableUtils.createTable(connectionSource, ContactImage.class);
			TableUtils.createTable(connectionSource, Company.class);
			TableUtils.createTable(connectionSource, CompanyLogo.class);
			TableUtils.createTable(connectionSource, Talk.class);
			TableUtils.createTable(connectionSource, TalkTrack.class);
			TableUtils.createTable(connectionSource, Venue.class);

			TableUtils.createTable(connectionSource, BeaconInfo.class);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			TableUtils.dropTable(connectionSource, Booth.class, true);
			TableUtils.dropTable(connectionSource, BoothContact.class, true);
			TableUtils.dropTable(connectionSource, Contact.class, true);
			TableUtils.dropTable(connectionSource, ContactImage.class, true);
			TableUtils.dropTable(connectionSource, Company.class, true);
			TableUtils.dropTable(connectionSource, CompanyLogo.class, true);
			TableUtils.dropTable(connectionSource, Talk.class, true);
			TableUtils.dropTable(connectionSource, TalkTrack.class, true);
			TableUtils.dropTable(connectionSource, Venue.class, true);

			TableUtils.dropTable(connectionSource, BeaconInfo.class, true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		onCreate(database, connectionSource);
	}
}
