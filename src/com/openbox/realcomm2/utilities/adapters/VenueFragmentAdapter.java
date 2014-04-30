package com.openbox.realcomm2.utilities.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.openbox.realcomm2.database.models.VenueModel;
import com.openbox.realcomm2.listingpage.VenueFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class VenueFragmentAdapter extends FragmentPagerAdapter
{
	List<VenueModel> venueList = new ArrayList<VenueModel>();
	private Date talkDate;
	
	public VenueFragmentAdapter(FragmentManager fm)
	{
		super(fm);
	}
	
	public void setItems(Date talkDate, List<VenueModel> venueList)
	{
		if (talkDate != null && venueList != null)
		{
			this.talkDate = talkDate;
			this.venueList = venueList;
			notifyDataSetChanged();
		}
	}

	@Override
	public Fragment getItem(int location)
	{
		// TODO: Maybe sort or something
		return VenueFragment.newInstance(this.talkDate, this.venueList.get(location).getVenueId());
	}

	@Override
	public int getCount()
	{
		return this.venueList.size();
	}

}
