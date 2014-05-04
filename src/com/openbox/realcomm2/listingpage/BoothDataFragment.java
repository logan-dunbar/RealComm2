package com.openbox.realcomm2.listingpage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.openbox.realcomm2.database.models.BoothModel;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.utilities.enums.BoothDataFragmentStatus;
import com.openbox.realcomm2.utilities.enums.BoothSortMode;
import com.openbox.realcomm2.utilities.enums.ProximityRegion;
import com.openbox.realcomm2.utilities.interfaces.BoothDataInterface;
import com.openbox.realcomm2.utilities.interfaces.BoothDataChangedCallbacks;
import com.openbox.realcomm2.utilities.loaders.BoothLoader;
import com.openbox.realcomm2.utilities.loaders.CompanyLoader;
import com.radiusnetworks.ibeacon.IBeacon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class BoothDataFragment extends Fragment implements BoothDataInterface
{
	private static final int BOOTH_LOADER_ID = 1;
	private static final int COMPANY_LOADER_ID = 2;

	private BoothDataChangedCallbacks dataChangedlistener;

	private List<BoothViewModel> boothViewModelArray = new ArrayList<>();

	private BoothDataFragmentStatus currentStatus;

	public static BoothDataFragment newInstance()
	{
		BoothDataFragment fragment = new BoothDataFragment();
		return fragment;
	}

	private void initBoothLoader()
	{
		getLoaderManager().initLoader(BOOTH_LOADER_ID, null, this.boothLoaderCallbacks);
	}

	private void initCompanyLoader()
	{
		getLoaderManager().initLoader(COMPANY_LOADER_ID, null, this.companyLoaderCallbacks);
	}

	/**********************************************************************************************
	 * Lifecycle implements
	 **********************************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.currentStatus = BoothDataFragmentStatus.Initializing;

		setRetainInstance(true);

		initBoothLoader();
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof BoothDataChangedCallbacks)
		{
			this.dataChangedlistener = (BoothDataChangedCallbacks) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();

		// Clean up
		this.dataChangedlistener = null;
	}

	/**********************************************************************************************
	 * Booth Data Interface Implements
	 **********************************************************************************************/
	@Override
	public BoothDataFragmentStatus getDataFragmentStatus()
	{
		return this.currentStatus;
	}

	@Override
	public void refreshData()
	{
		// Only refresh Booth loader, as it triggers Company loader in onLoadFinished()
		Loader<List<BoothViewModel>> boothLoader = getLoaderManager().getLoader(BOOTH_LOADER_ID);
		if (boothLoader == null)
		{
			initBoothLoader();
		}
		else
		{
			boothLoader.onContentChanged();
		}
	}

	@Override
	public BoothViewModel getBoothViewModel(int boothId)
	{
		for (BoothViewModel viewModel : this.boothViewModelArray)
		{
			if (viewModel.getBooth() != null && viewModel.getBooth().getBoothId() == boothId)
			{
				return viewModel;
			}
		}

		return null;
	}

	@Override
	public List<BoothViewModel> getBooothViewModels()
	{
		return this.boothViewModelArray;
	}

	@Override
	public List<BoothViewModel> getBoothViewModels(BoothSortMode boothSortMode)
	{
		switch (boothSortMode)
		{
			case Distance:
				return getListSortedByDistance();
			case Name:
				return getListSortedByName();
			default:
				return getBooothViewModels();
		}
	}

	private List<BoothViewModel> getListSortedByDistance()
	{
		List<BoothViewModel> sortedList = new ArrayList<>(this.boothViewModelArray);
		Collections.sort(sortedList, new Comparator<BoothViewModel>()
		{
			@Override
			public int compare(BoothViewModel lhs, BoothViewModel rhs)
			{
				Double lhsAccuracy = lhs.getBooth().getAccuracy();
				Double rhsAccuracy = rhs.getBooth().getAccuracy();

				if (lhsAccuracy < rhsAccuracy)
				{
					// 50 - 23
					return -1;
				}
				else if (lhsAccuracy > rhsAccuracy)
				{
					return 1;
				}
				else
				{
					// Default to name sorting
					return lhs.getCompany().getCompanyName().compareToIgnoreCase(rhs.getCompany().getCompanyName());
				}
			}
		});

		return sortedList;
	}

	private List<BoothViewModel> getListSortedByName()
	{
		List<BoothViewModel> sortedList = new ArrayList<>(this.boothViewModelArray);
		Collections.sort(sortedList, new Comparator<BoothViewModel>()
		{
			@Override
			public int compare(BoothViewModel lhs, BoothViewModel rhs)
			{
				return lhs.getCompany().getCompanyName().compareToIgnoreCase(rhs.getCompany().getCompanyName());
			}
		});

		return sortedList;
	}

	@Override
	public void updateBoothDistances(Collection<IBeacon> beaconList)
	{
		for (BoothViewModel viewModel : this.boothViewModelArray)
		{
			Boolean outOfRange = true;
			BoothModel booth = viewModel.getBooth();
			for (IBeacon beacon : beaconList)
			{
				if (booth.getUuid().equalsIgnoreCase(beacon.getProximityUuid()) &&
					booth.getMajor() == beacon.getMajor() &&
					booth.getMinor() == beacon.getMinor())
				{
					booth.updateDistanceWithBeacon(beacon);
					outOfRange = false;
					break;
				}
			}

			if (outOfRange)
			{
				booth.updateDistanceWithDefault();
			}
		}

		if (this.dataChangedlistener != null)
		{
			this.dataChangedlistener.onBoothDataUpdated();
		}
	}

	@Override
	public void resetBoothDistances()
	{
		for (BoothViewModel viewModel : this.boothViewModelArray)
		{
			viewModel.getBooth().resetDistance();
		}

		if (this.dataChangedlistener != null)
		{
			this.dataChangedlistener.onBoothDataUpdated();
		}
	}

	@Override
	public List<Integer> getClosestBoothIds(int numberOfDisplayBooths)
	{
		List<Integer> boothIds = new ArrayList<Integer>();
		List<BoothViewModel> sortedList = getListSortedByDistance();
		for (int i = 0; i < getNumberOfBooths(numberOfDisplayBooths); i++)
		{
			boothIds.add(sortedList.get(i).getBooth().getBoothId());
		}

		return boothIds;
	}

	@Override
	public List<Integer> getRandomBoothIds(int numberOfDisplayBooths)
	{
		Random random = new Random();
		List<Integer> randomList = new ArrayList<Integer>();
		List<Integer> boothIds = new ArrayList<Integer>();
		for (int i = 0; i < getNumberOfBooths(numberOfDisplayBooths); i++)
		{
			int position;
			do
			{
				// Make sure to random on full list
				position = random.nextInt(this.boothViewModelArray.size());
			}
			while (randomList.contains(position));

			randomList.add(position);
			boothIds.add(this.boothViewModelArray.get(position).getBooth().getBoothId());
		}

		return boothIds;
	}

	@Override
	public Map<ProximityRegion, Integer> getBoothProximityCounts()
	{
		// Get all the proximity regions
		Map<ProximityRegion, Integer> proximityCounts = new HashMap<ProximityRegion, Integer>();
		for (ProximityRegion proximityRegion : ProximityRegion.values())
		{
			proximityCounts.put(proximityRegion, 0);
		}

		// Update the counts for the proximity regions
		for (BoothViewModel viewModel : this.boothViewModelArray)
		{
			BoothModel booth = viewModel.getBooth();
			ProximityRegion boothProximityRegion = booth.getProximityRegion();
			proximityCounts.put(boothProximityRegion, proximityCounts.get(boothProximityRegion) + 1);
		}

		// TODO might need to make sure of the ordering here
		// (shouldn't though, as Enum.values() returns ordered list as declared in enum)
		return proximityCounts;
	}

	private int getNumberOfBooths(int numberOfDisplayBooths)
	{
		return this.boothViewModelArray.size() < numberOfDisplayBooths ? this.boothViewModelArray.size() : numberOfDisplayBooths;
	}

	/**********************************************************************************************
	 * Loader Callbacks
	 **********************************************************************************************/
	private LoaderCallbacks<List<BoothViewModel>> boothLoaderCallbacks = new LoaderCallbacks<List<BoothViewModel>>()
	{
		@Override
		public Loader<List<BoothViewModel>> onCreateLoader(int loaderId, Bundle bundle)
		{
			return new BoothLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<List<BoothViewModel>> loader, List<BoothViewModel> boothViewModelArray)
		{
			BoothDataFragment.this.boothViewModelArray = boothViewModelArray;
			if (BoothDataFragment.this.dataChangedlistener != null)
			{
				// Booths loaded, can start ranging and updating distance values, as well as start loading companies
				BoothDataFragment.this.currentStatus = BoothDataFragmentStatus.BoothsLoaded;
				initCompanyLoader();
				if (BoothDataFragment.this.dataChangedlistener != null)
				{
					BoothDataFragment.this.dataChangedlistener.onBoothDataLoaded();
				}
			}
		}

		@Override
		public void onLoaderReset(Loader<List<BoothViewModel>> loader)
		{
		}
	};

	private LoaderCallbacks<List<BoothViewModel>> companyLoaderCallbacks = new LoaderCallbacks<List<BoothViewModel>>()
	{
		@Override
		public Loader<List<BoothViewModel>> onCreateLoader(int loaderId, Bundle bundle)
		{
			return new CompanyLoader(getActivity(), BoothDataFragment.this.boothViewModelArray);
		}

		@Override
		public void onLoadFinished(Loader<List<BoothViewModel>> loader, List<BoothViewModel> boothViewModelArray)
		{
			// Booths and Companies loaded
			BoothDataFragment.this.currentStatus = BoothDataFragmentStatus.BoothsAndContactsLoaded;
			BoothDataFragment.this.boothViewModelArray = boothViewModelArray;
			if (BoothDataFragment.this.dataChangedlistener != null)
			{
				BoothDataFragment.this.dataChangedlistener.onCompanyDataLoaded();
			}
		}

		@Override
		public void onLoaderReset(Loader<List<BoothViewModel>> loader)
		{
		}
	};

	// TODO: Will be removed
	@Override
	public BoothModel getBooth(int index)
	{
		if (this.currentStatus != BoothDataFragmentStatus.BoothsAndContactsLoaded)
		{
			return null;
		}

		return getListSortedByDistance().get(index).getBooth();
	}
}
