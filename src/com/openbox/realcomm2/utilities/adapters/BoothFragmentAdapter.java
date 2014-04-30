package com.openbox.realcomm2.utilities.adapters;

import java.util.ArrayList;
import java.util.List;

import com.openbox.realcomm2.database.models.BoothDistanceModel;
import com.openbox.realcomm2.listingpage.BoothFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BoothFragmentAdapter extends FragmentStatePagerAdapter
{
	private List<BoothDistanceModel> boothDistanceList = new ArrayList<BoothDistanceModel>();
	
	public BoothFragmentAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
	public void setItems(List<BoothDistanceModel> boothDistanceList)
	{
		if (boothDistanceList != null)
		{
			this.boothDistanceList = boothDistanceList;
		}
	}

	@Override
	public Fragment getItem(int location)
	{
		return BoothFragment.newInstance(this.boothDistanceList.get(location).getBoothId(), true);
	}

	@Override
	public int getCount()
	{
		return this.boothDistanceList.size();
	}
}
