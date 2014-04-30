package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CompanyLogo")
public class CompanyLogo implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = -6228227879672944042L;

	public static final Class<?> ID_CLASS = Integer.class;

	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String COMPANY_LOGO_ID_COLUMN_NAME = "CompanyLogoId";
	public static final String LOGO_COLUMN_NAME = "Logo";

	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(id = true, columnName = COMPANY_LOGO_ID_COLUMN_NAME)
	@SerializedName(COMPANY_LOGO_ID_COLUMN_NAME)
	private int companyLogoId;

	@DatabaseField(columnName = Company.COMPANY_ID_COLUMN_NAME)
	@SerializedName(Company.COMPANY_ID_COLUMN_NAME)
	private int companyId;

	@DatabaseField(columnName = LOGO_COLUMN_NAME)
	@SerializedName(LOGO_COLUMN_NAME)
	private String logo;
	
	public CompanyLogo()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}
	
	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// CompanyLogo
	public int getCompanyLogoId()
	{
		return companyLogoId;
	}

	public void setCompanyLogoId(int companyLogoId)
	{
		this.companyLogoId = companyLogoId;
	}

	// CompanyId
	public int getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(int companyId)
	{
		this.companyId = companyId;
	}

	// Logo
	public String getLogo()
	{
		return logo;
	}
	
	public byte[] getLogoBytes()
	{
		return Base64.decode(this.logo, Base64.DEFAULT);
	}

	public void setLogo(String logo)
	{
		this.logo = logo;
	}
}
