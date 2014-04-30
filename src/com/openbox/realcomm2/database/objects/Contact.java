package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Contact")
public class Contact implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = 1129292605327597677L;

	public static final Class<?> ID_CLASS = Integer.class;

	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String CONTACT_ID_COLUMN_NAME = "ContactId";
	public static final String FIRST_NAME_COLUMN_NAME = "FirstName";
	public static final String LAST_NAME_COLUMN_NAME = "LastName";
	public static final String JOB_DESCRIPTION_COLUMN_NAME = "JobDescription";
	public static final String IS_PRIMARY_CONTACT_COLUMN_NAME = "IsPrimaryContact";
	public static final String EMAIL_COLUMN_NAME = "Email";
	public static final String CONTACT_NUMBER_COLUMN_NAME = "ContactNumber";

	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(id = true, columnName = CONTACT_ID_COLUMN_NAME)
	@SerializedName(CONTACT_ID_COLUMN_NAME)
	private int contactId;
	
	@DatabaseField(columnName = Company.COMPANY_ID_COLUMN_NAME)
	@SerializedName(Company.COMPANY_ID_COLUMN_NAME)
	private int companyId;

	@DatabaseField(columnName = FIRST_NAME_COLUMN_NAME)
	@SerializedName(FIRST_NAME_COLUMN_NAME)
	private String firstName;

	@DatabaseField(columnName = LAST_NAME_COLUMN_NAME)
	@SerializedName(LAST_NAME_COLUMN_NAME)
	private String lastName;

	@DatabaseField(columnName = JOB_DESCRIPTION_COLUMN_NAME)
	@SerializedName(JOB_DESCRIPTION_COLUMN_NAME)
	private String jobDescription;

	@DatabaseField(columnName = IS_PRIMARY_CONTACT_COLUMN_NAME)
	@SerializedName(IS_PRIMARY_CONTACT_COLUMN_NAME)
	private Boolean isPrimaryContact;

	@DatabaseField(columnName = EMAIL_COLUMN_NAME)
	@SerializedName(EMAIL_COLUMN_NAME)
	private String email;

	@DatabaseField(columnName = CONTACT_NUMBER_COLUMN_NAME)
	@SerializedName(CONTACT_NUMBER_COLUMN_NAME)
	private String contactNumber;

	public Contact()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// ContactId
	public int getContactId()
	{
		return this.contactId;
	}

	public void setContactId(int contactId)
	{
		this.contactId = contactId;
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

	// FirstName
	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	// LastName
	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	// JobDescription
	public String getJobDescription()
	{
		return this.jobDescription;
	}

	public void setJobDescription(String jobDescription)
	{
		this.jobDescription = jobDescription;
	}

	// IsPrimaryContact
	public Boolean getIsPrimaryContact()
	{
		return this.isPrimaryContact;
	}

	public void setIsPrimaryContact(Boolean isPrimaryContact)
	{
		this.isPrimaryContact = isPrimaryContact;
	}

	// Email
	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	// ContactNumber
	public String getContactNumber()
	{
		return this.contactNumber;
	}

	public void setContactNumber(String contactNumber)
	{
		this.contactNumber = contactNumber;
	}

	// Misc Funcs
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(firstName + ", ");
		builder.append(lastName + ", ");
		builder.append(jobDescription + ", ");
		builder.append(isPrimaryContact + ", ");
		builder.append(email + ", ");
		builder.append(contactNumber);
		return builder.toString();
	}
}
