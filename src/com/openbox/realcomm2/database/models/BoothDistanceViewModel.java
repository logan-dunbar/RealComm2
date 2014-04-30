package com.openbox.realcomm2.database.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.radiusnetworks.ibeacon.IBeacon;

public class BoothDistanceViewModel
{
	private static final float IMMEDIATE_CUTOFF = 3.0f;
	private static final float NEAR_CUTOFF = 8.0f;
	private static final float FAR_CUTOFF = 15.0f;
	
	private List<BoothDistanceModel> boothDistanceList;
	
	private static AtomicInteger updateCount = new AtomicInteger(0);
	
	private Boolean getWindowLoaded()
	{
		return updateCount.get() >= BoothDistanceModel.WINDOW_LENGTH;
	}
	
	/**********************************************************************************************
	 * Constructor
	 **********************************************************************************************/
	public BoothDistanceViewModel()
	{
		this.boothDistanceList = new ArrayList<BoothDistanceModel>();
	}
	
	public void setItems(List<BoothDistanceModel> boothDistanceList)
	{
		this.boothDistanceList = boothDistanceList;
	}

	/**********************************************************************************************
	 * Public Methods
	 **********************************************************************************************/
	// Updates every booth in the list with the sampled RSSI value, or passes null (to set default)
	public void updateBoothDistanceList(List<IBeacon> beaconList)
	{
		for (BoothDistanceModel boothDistanceModel : this.boothDistanceList)
		{
			Float rssi = null;
			Float accuracy = null;
			for (IBeacon beacon : beaconList)
			{
				if (boothDistanceModel.getMajor() == beacon.getMajor() && boothDistanceModel.getMinor() == beacon.getMinor())
				{
					rssi = (float) beacon.getRssi();
					accuracy = (float) beacon.getAccuracy();
					continue;
				}
			}

			boothDistanceModel.addRssi(rssi);
			boothDistanceModel.addAccuracy(accuracy);
		}

		// TODO: Change the sort method/tune it to include accuracy figures
		// Sort by distance (using custom sort)
		Collections.sort(this.boothDistanceList);
		if (!getWindowLoaded())
		{
			updateCount.incrementAndGet();
		}
	}
	
	public List<Integer> getClosestBoothIds(int number)
	{
		// Check if window finished loading before returning the booths
		if (getWindowLoaded())
		{
			return getBoothIds(number);
		}
		else
		{
			return new ArrayList<Integer>();
		}
	}
	
	private List<Integer> getBoothIds(int number)
	{
		int max = number;
		if (this.boothDistanceList.size() < number)
		{
			max = this.boothDistanceList.size();
		}

		// List is sorted after update, so can just grab the top n items
		List<Integer> boothIds = new ArrayList<Integer>();
		for (int i = 0; i < max; i++)
		{
			boothIds.add(this.boothDistanceList.get(i).getBoothId());
		}
		
		return boothIds;
	}
	
	// TODO: Might not need this
	public List<BoothDistanceModel> getImmediateBooths()
	{
		return getBoothsForRange(0.0f, IMMEDIATE_CUTOFF);
	}
	
	public List<BoothDistanceModel> getNearBooths()
	{
		return getBoothsForRange(IMMEDIATE_CUTOFF, NEAR_CUTOFF);
	}
	
	public List<BoothDistanceModel> getFarBooths()
	{
		return getBoothsForRange(NEAR_CUTOFF, FAR_CUTOFF);
	}
	
	private List<BoothDistanceModel> getBoothsForRange(float start, float end)
	{
		List<BoothDistanceModel> list = new ArrayList<BoothDistanceModel>();
		for (int i = 0; i < this.boothDistanceList.size(); i++)
		{
			BoothDistanceModel model = this.boothDistanceList.get(i);
			if (model.getAverageAccuracy() < start)
			{
				// do nothing
			}
			else if (model.getAverageAccuracy() >= start && model.getAverageAccuracy() < end)
			{
				// in range
				list.add(model);
			}
			else
			{
				// list is sorted, so can end the loop here
				break;
			}
		}
		
		return list;
	}
	
	// TODO: Will be removed
	public BoothDistanceModel getBooth(int position)
	{
		return this.boothDistanceList.get(position);
	}
}
