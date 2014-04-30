package com.openbox.realcomm2.listingpage;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.application.RealCommApplication;
import com.openbox.realcomm2.controls.ClearableEditText;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.utilities.adapters.BoothListAdapter;
import com.openbox.realcomm2.utilities.enums.BoothSortMode;
import com.openbox.realcomm2.utilities.interfaces.BoothDataInterface;
import com.openbox.realcomm2.utilities.interfaces.BoothDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BoothListInterface;
import com.openbox.realcomm2.utilities.interfaces.ListingPageCallbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BoothListFragment extends Fragment implements TextWatcher, OnItemClickListener, BoothDataChangedCallbacks, BoothListInterface
{
	private static final String BOOTH_SORT_MODE_KEY = "boothSortModeKey";

	private ListingPageCallbacks listingPageListener;
	private BoothDataInterface boothDataListener;

	private ClearableEditText boothFilter;
	private ListView boothListView;
	private BoothListAdapter boothAdapter;

	private BoothSortMode currentSortMode;

	public static BoothListFragment newInstance()
	{
		BoothListFragment fragment = new BoothListFragment();

		Bundle args = new Bundle();
		fragment.setArguments(args);

		return fragment;
	}

	/**********************************************************************************************
	 * Lifecycle Implements
	 **********************************************************************************************/
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof ListingPageCallbacks)
		{
			this.listingPageListener = (ListingPageCallbacks) activity;
		}

		if (activity instanceof BoothDataInterface)
		{
			this.boothDataListener = (BoothDataInterface) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();

		// Clean up
		this.listingPageListener = null;
		this.boothDataListener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.booth_list_fragment, container, false);

		RealCommApplication application = (RealCommApplication) getActivity().getApplication();

		this.boothFilter = (ClearableEditText) view.findViewById(R.id.boothFilter);
		this.boothFilter.addTextChangedListener(this);
		this.boothFilter.setTypeface(application.getExo2Font());

		this.boothListView = (ListView) view.findViewById(R.id.boothListView);
		this.boothListView.setOnItemClickListener(this);

		this.boothAdapter = new BoothListAdapter(getActivity());
		this.boothListView.setAdapter(this.boothAdapter);

		// TODO: check this default
		this.currentSortMode = BoothSortMode.Name;
		if (savedInstanceState != null)
		{
			this.currentSortMode = (BoothSortMode) savedInstanceState.getSerializable(BOOTH_SORT_MODE_KEY);
		}

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putSerializable(BOOTH_SORT_MODE_KEY, this.currentSortMode);
	}

	/**********************************************************************************************
	 * TextWatcher Implements
	 **********************************************************************************************/
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		this.boothAdapter.filterItems(s.toString());
	}

	@Override
	public void afterTextChanged(Editable s)
	{
	}

	/**********************************************************************************************
	 * Click Implements
	 **********************************************************************************************/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		BoothViewModel viewModel = (BoothViewModel) this.boothListView.getItemAtPosition(position);
		if (this.listingPageListener != null)
		{
			this.listingPageListener.goToProfilePage(viewModel.getCompany().getCompanyId());
		}
	}

	/**********************************************************************************************
	 * Booth List Implements
	 **********************************************************************************************/
	@Override
	public void updateList()
	{
		if (this.boothDataListener != null && this.listingPageListener != null)
		{
			this.boothAdapter.setCurrentAppMode(this.listingPageListener.getCurrentAppMode());
			this.boothAdapter.setItems(this.boothDataListener.getBoothViewModels(this.currentSortMode));
		}
	}

	@Override
	public void toggleSortMode()
	{
		this.currentSortMode = this.currentSortMode == BoothSortMode.Distance ? BoothSortMode.Name : BoothSortMode.Distance;
		updateList();
	}

	/**********************************************************************************************
	 * Booth Data Changed Callbacks
	 **********************************************************************************************/
	@Override
	public void onBoothDataLoaded()
	{
		// Do nothing, not ready yet
	}

	@Override
	public void onCompanyDataLoaded()
	{
		updateList();
	}

	@Override
	public void onBoothDataUpdated()
	{
		updateList();
	}

	/**********************************************************************************************
	 * Clear Focus Implements
	 **********************************************************************************************/
	@Override
	public View getViewToClearFocus()
	{
		return this.boothFilter;
	}
}
