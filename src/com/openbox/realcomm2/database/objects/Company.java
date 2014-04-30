package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Company")
public class Company implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = 1566568241666455317L;

	public static final Class<?> ID_CLASS = Integer.class;

	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String COMPANY_ID_COLUMN_NAME = "CompanyId";
	public static final String NAME_COLUMN_NAME = "Name";
	public static final String DESCRIPTION_COLUMN_NAME = "Description";
	public static final String WEBSITE_COLUMN_NAME = "Website";
	public static final String FACEBOOK_COLUMN_NAME = "Facebook";
	public static final String TWITTER_COLUMN_NAME = "Twitter";
	public static final String LINKEDIN_COLUMN_NAME = "LinkedIn";

	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(id = true, columnName = COMPANY_ID_COLUMN_NAME)
	@SerializedName(COMPANY_ID_COLUMN_NAME)
	private int companyId;

	@DatabaseField(columnName = NAME_COLUMN_NAME)
	@SerializedName(NAME_COLUMN_NAME)
	private String name;

	@DatabaseField(columnName = DESCRIPTION_COLUMN_NAME)
	@SerializedName(DESCRIPTION_COLUMN_NAME)
	private String description;

	@DatabaseField(columnName = WEBSITE_COLUMN_NAME)
	@SerializedName(WEBSITE_COLUMN_NAME)
	private String website;
	
	@DatabaseField(columnName = FACEBOOK_COLUMN_NAME)
	@SerializedName(FACEBOOK_COLUMN_NAME)
	private String facebook;
	
	@DatabaseField(columnName = TWITTER_COLUMN_NAME)
	@SerializedName(TWITTER_COLUMN_NAME)
	private String twitter;
	
	@DatabaseField(columnName = LINKEDIN_COLUMN_NAME)
	@SerializedName(LINKEDIN_COLUMN_NAME)
	private String linkedIn;

	public Company()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// CompanyId
	public int getCompanyId()
	{
		return this.companyId;
	}

	public void setCompanyId(int companyId)
	{
		this.companyId = companyId;
	}

	// Name
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// Description
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	// Website
	public String getWebsite()
	{
		return this.website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	// Facebook
	public String getFacebook()
	{
		return facebook;
	}

	public void setFacebook(String facebook)
	{
		this.facebook = facebook;
	}

	// Twitter
	public String getTwitter()
	{
		return twitter;
	}

	public void setTwitter(String twitter)
	{
		this.twitter = twitter;
	}

	// LinkedIn
	public String getLinkedIn()
	{
		return linkedIn;
	}

	public void setLinkedIn(String linkedIn)
	{
		this.linkedIn = linkedIn;
	}
}
