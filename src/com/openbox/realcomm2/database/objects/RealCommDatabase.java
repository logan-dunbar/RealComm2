package com.openbox.realcomm2.database.objects;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RealCommDatabase implements Serializable
{
	private static final long serialVersionUID = -2179729486615337237L;

	/**********************************************************************************************
	 * Object Names
	 **********************************************************************************************/
	public static final String BOOTH_LIST_OBJECT_NAME = "BoothList";
	public static final String BOOTH_CONTACT_LIST_OBJECT_NAME = "BoothContactList";
	public static final String CONTACT_LIST_OBJECT_NAME = "ContactList";
	public static final String CONTACT_IMAGE_LIST_OBJECT_NAME = "ContactImageList";
	public static final String COMPANY_LIST_OBJECT_NAME = "CompanyList";
	public static final String COMPANY_LOGO_LIST_OBJECT_NAME = "CompanyLogoList";
	public static final String TALK_LIST_OBJECT_NAME = "TalkList";
	public static final String TALK_TRACK_LIST_OBJECT_NAME = "TalkTrackList";
	public static final String VENUE_LIST_OBJECT_NAME = "VenueList";

	@SerializedName(BOOTH_LIST_OBJECT_NAME)
	private List<Booth> boothList;

	@SerializedName(BOOTH_CONTACT_LIST_OBJECT_NAME)
	private List<BoothContact> boothContactList;

	@SerializedName(CONTACT_LIST_OBJECT_NAME)
	private List<Contact> contactList;

	@SerializedName(CONTACT_IMAGE_LIST_OBJECT_NAME)
	private List<ContactImage> contactImageList;

	@SerializedName(COMPANY_LIST_OBJECT_NAME)
	private List<Company> companyList;

	@SerializedName(COMPANY_LOGO_LIST_OBJECT_NAME)
	private List<CompanyLogo> companyLogoList;

	@SerializedName(TALK_LIST_OBJECT_NAME)
	private List<Talk> talkList;

	@SerializedName(TALK_TRACK_LIST_OBJECT_NAME)
	private List<TalkTrack> talkTrackList;

	@SerializedName(VENUE_LIST_OBJECT_NAME)
	private List<Venue> venueList;

	/**********************************************************************************************
	 * Property Get and Setters
	 **********************************************************************************************/
	public List<Booth> getBoothList()
	{
		return boothList;
	}

	public void setBoothList(List<Booth> boothList)
	{
		this.boothList = boothList;
	}

	public List<BoothContact> getBoothContactList()
	{
		return boothContactList;
	}

	public void setBoothContactList(List<BoothContact> boothContactList)
	{
		this.boothContactList = boothContactList;
	}

	public List<Contact> getContactList()
	{
		return contactList;
	}

	public void setContactList(List<Contact> contactList)
	{
		this.contactList = contactList;
	}

	public List<ContactImage> getContactImageList()
	{
		return contactImageList;
	}

	public void setContactImageList(List<ContactImage> contactImageList)
	{
		this.contactImageList = contactImageList;
	}

	public List<Company> getCompanyList()
	{
		return companyList;
	}

	public void setCompanyList(List<Company> companyList)
	{
		this.companyList = companyList;
	}

	public List<CompanyLogo> getCompanyLogoList()
	{
		return companyLogoList;
	}

	public void setCompanyLogoList(List<CompanyLogo> companyLogoList)
	{
		this.companyLogoList = companyLogoList;
	}

	public List<Talk> getTalkList()
	{
		return talkList;
	}

	public void setTalkList(List<Talk> talkList)
	{
		this.talkList = talkList;
	}

	public List<TalkTrack> getTalkTrackList()
	{
		return talkTrackList;
	}

	public void setTalkTrackList(List<TalkTrack> talkTrackList)
	{
		this.talkTrackList = talkTrackList;
	}

	public List<Venue> getVenueList()
	{
		return venueList;
	}

	public void setVenueList(List<Venue> venueList)
	{
		this.venueList = venueList;
	}
}
