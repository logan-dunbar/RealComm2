package com.openbox.realcomm2.database.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.openbox.realcomm2.database.objects.Company;
import com.openbox.realcomm2.database.objects.CompanyLogo;

public class CompanyModel
{

	private int companyId;
	private String companyName;
	private String companyDetails;
	private Bitmap companyLogo;

	public CompanyModel(Company company, CompanyLogo companyLogo)
	{
		if (company != null && companyLogo != null)
		{
			this.companyId = company.getCompanyId();
			this.companyName = company.getName();
			this.companyDetails = company.getDescription();
			this.companyLogo = BitmapFactory.decodeByteArray(companyLogo.getLogoBytes(), 0, companyLogo.getLogoBytes().length);
		}
	}

	public int getCompanyId()
	{
		return companyId;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public String getCompanyDetails()
	{
		return companyDetails;
	}

	public Bitmap getCompanyLogo()
	{
		return companyLogo;
	}

}
