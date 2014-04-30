package com.openbox.realcomm2.database.models;

public class BoothViewModel
{
	private BoothModel booth;
	private CompanyModel company;

	public BoothViewModel(BoothModel booth)
	{
		this.booth = booth;
	}

	public BoothModel getBooth()
	{
		return booth;
	}

	public void setBooth(BoothModel booth)
	{
		this.booth = booth;
	}

	public CompanyModel getCompany()
	{
		return company;
	}

	public void setCompany(CompanyModel company)
	{
		this.company = company;
	}

	public Boolean isCompanyLoaded()
	{
		return this.company != null;
	}
}
