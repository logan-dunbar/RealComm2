package com.openbox.realcomm2.utilities.loaders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.openbox.realcomm2.database.DatabaseManager;
import com.openbox.realcomm2.database.models.BoothModel;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.database.models.CompanyModel;
import com.openbox.realcomm2.database.objects.Booth;
import com.openbox.realcomm2.database.objects.BoothContact;
import com.openbox.realcomm2.database.objects.Company;
import com.openbox.realcomm2.database.objects.CompanyLogo;
import com.openbox.realcomm2.database.objects.Contact;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class CompanyLoader extends AsyncTaskLoader<List<BoothViewModel>>
{
	private List<BoothViewModel> outboothViewModelList;
	private List<BoothViewModel> inBoothViewModelList;

	public CompanyLoader(Context context, List<BoothViewModel> boothViewModelArray)
	{
		super(context);
		this.inBoothViewModelList = boothViewModelArray;
	}

	@Override
	protected void onStartLoading()
	{
		// If we currently have a result available, deliver it immediately.
		if (this.outboothViewModelList != null)
		{
			deliverResult(this.outboothViewModelList);
		}
		else
		{
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading()
	{
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onContentChanged()
	{
		// We were notified that content has changed, force a load.
		forceLoad();
	}

	@Override
	public void reset()
	{
		super.reset();

		// Ensure the loader is stopped
		onStopLoading();

		this.outboothViewModelList = null;
		this.inBoothViewModelList = null;
	}

	@Override
	public void deliverResult(List<BoothViewModel> data)
	{
		this.outboothViewModelList = data;
		this.inBoothViewModelList = null;

		// If the Loader is currently started, we can immediately deliver its results.
		if (isStarted())
		{
			super.deliverResult(data);
		}
	}

	@Override
	public List<BoothViewModel> loadInBackground()
	{
		// TODO: look into splitting company up so that images are separate.
		// TODO: Load cycle will go: Booth -> Company Details -> Company Logo
		// TODO: First 2 will be super quick, so can show the info quick, then populate images as they arrive
		for (BoothViewModel viewModel : this.inBoothViewModelList)
		{
			BoothModel booth = viewModel.getBooth();

			Company company = null;
			CompanyLogo companyLogo = null;

			try
			{
				List<BoothContact> boothContactList;
				boothContactList = DatabaseManager.getInstance().getForWhereEquals(BoothContact.class, Booth.BOOTH_ID_COLUMN_NAME, booth.getBoothId());

				List<Integer> contactIdList = new ArrayList<Integer>();
				for (BoothContact boothContact : boothContactList)
				{
					contactIdList.add(boothContact.getContactId());
				}

				List<Contact> contactList = DatabaseManager.getInstance().getForWhereIn(Contact.class, Contact.CONTACT_ID_COLUMN_NAME, contactIdList);

				List<Integer> companyIdList = new ArrayList<Integer>();
				for (Contact contact : contactList)
				{
					companyIdList.add(contact.getCompanyId());
				}

				// This does a distinct
				companyIdList = new ArrayList<Integer>(new HashSet<Integer>(companyIdList));

				// Should only be one company, might need to cater for multiple, but ultimate edge case
				if (companyIdList.size() > 0)
				{
					int companyId = companyIdList.get(0);

					// Get the company
					company = DatabaseManager.getInstance().getForId(Company.class, companyId);

					// Get company logo
					List<CompanyLogo> companyLogoList = DatabaseManager.getInstance().getForWhereEquals(CompanyLogo.class, Company.COMPANY_ID_COLUMN_NAME,
						companyId);

					if (companyLogoList.size() > 0)
					{
						companyLogo = companyLogoList.get(0);
					}
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}

			viewModel.setCompany(new CompanyModel(company, companyLogo));
		}

		return this.inBoothViewModelList;
	}

}
