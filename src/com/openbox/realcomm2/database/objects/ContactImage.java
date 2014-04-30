package com.openbox.realcomm2.database.objects;

import java.io.Serializable;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ContactImage")
public class ContactImage implements Serializable
{
	// Generated Serializable UID
	private static final long serialVersionUID = -6468565926109967656L;

	public static final Class<?> ID_CLASS = Integer.class;
	
	/**********************************************************************************************
	 * Column Names
	 **********************************************************************************************/
	public static final String CONTACT_IMAGE_ID_COLUMN_NAME = "ContactImageId";
	public static final String IMAGE_COLUMN_NAME = "Image";
	
	/**********************************************************************************************
	 * Database Fields
	 **********************************************************************************************/
	@DatabaseField(id = true, columnName = CONTACT_IMAGE_ID_COLUMN_NAME)
	@SerializedName(CONTACT_IMAGE_ID_COLUMN_NAME)
	private int contactImageId;

	@DatabaseField(columnName = Contact.CONTACT_ID_COLUMN_NAME)
	@SerializedName(Contact.CONTACT_ID_COLUMN_NAME)
	private int contactId;

	@DatabaseField(columnName = IMAGE_COLUMN_NAME)
	@SerializedName(IMAGE_COLUMN_NAME)
	private String image;
	
	public ContactImage()
	{
		// all persisted classes must define a no-arg constructor with at least package visibility
	}

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	// ContactImageId
	public int getContactImageId()
	{
		return this.contactImageId;
	}

	public void setContactImageId(int contactImageId)
	{
		this.contactImageId = contactImageId;
	}

	// ContactId
	public int getContactId()
	{
		return this.contactId;
	}

	public void setContactId(int contactId)
	{
		this.contactId = contactId;
	}

	// Image
	public String getImage()
	{
		return this.image;
	}
	
	public byte[] getImageBytes()
	{
		return Base64.decode(this.image, Base64.DEFAULT);
	}

	public void setImage(String image)
	{
		this.image = image;
	}
}
