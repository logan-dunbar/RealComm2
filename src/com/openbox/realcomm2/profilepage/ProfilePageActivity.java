package com.openbox.realcomm2.profilepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.base.BaseActivity;
import com.openbox.realcomm2.database.objects.Company;
import com.openbox.realcomm2.utilities.helpers.ToastHelper;

public class ProfilePageActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_page_activity);
		
		ToastHelper.showLongMessage(this, "Company Id=" + getIntent().getExtras().getInt(Company.COMPANY_ID_COLUMN_NAME) + " is showing");
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	public void onAttachFragment(Fragment fragment)
	{
		super.onAttachFragment(fragment);
	}
}
