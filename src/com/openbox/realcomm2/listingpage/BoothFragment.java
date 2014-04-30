package com.openbox.realcomm2.listingpage;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.application.RealCommApplication;
import com.openbox.realcomm2.database.models.BoothModel;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.database.models.CompanyModel;
import com.openbox.realcomm2.utilities.interfaces.BoothDataChangedCallbacks;
import com.openbox.realcomm2.utilities.interfaces.BoothDataInterface;
import com.openbox.realcomm2.utilities.interfaces.BoothInterface;
import com.openbox.realcomm2.utilities.interfaces.ListingPageCallbacks;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BoothFragment extends Fragment implements OnClickListener, BoothDataChangedCallbacks, BoothInterface
{
	private static final String BOOTH_ID_KEY = "boothIdKey";
	private static final String IS_BIG_KEY = "isBigKey";
	private static final String BOOTH_PREFIX = "BOOTH ";

	private ListingPageCallbacks listingPageListener;
	private BoothDataInterface boothDataListener;

	private BoothViewModel boothViewModel;

	private ImageView logo;
	private TextView header;
	private TextView subHeader;
	private TextView details;
	private Button viewProfileButton;

	public static BoothFragment newInstance(int boothId, Boolean isBig)
	{
		BoothFragment fragment = new BoothFragment();

		Bundle args = new Bundle();
		args.putInt(BOOTH_ID_KEY, boothId);
		args.putBoolean(IS_BIG_KEY, isBig);
		fragment.setArguments(args);

		return fragment;
	}

	public int getBoothId()
	{
		return getArguments().getInt(BOOTH_ID_KEY);
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
		View view = inflater.inflate(R.layout.booth_fragment, container, false);

		RealCommApplication application = (RealCommApplication) getActivity().getApplication();

		this.logo = (ImageView) view.findViewById(R.id.boothFragmentLogo);
		this.header = (TextView) view.findViewById(R.id.boothFragmentHeader);
		this.subHeader = (TextView) view.findViewById(R.id.boothFragmentSubHeader);
		this.details = (TextView) view.findViewById(R.id.boothFragmentDetails);

		this.header.setTypeface(application.getExo2Font());
		this.subHeader.setTypeface(application.getExo2FontBold());
		this.details.setTypeface(application.getExo2Font());

		this.viewProfileButton = (Button) view.findViewById(R.id.boothFragmentViewProfileButton);
		this.viewProfileButton.setTypeface(application.getExo2Font());
		
		updateBooth();

		return view;
	}
	
	/**********************************************************************************************
	 * Click Implements
	 **********************************************************************************************/
	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.boothFragmentViewProfileButton)
		{
			if (this.listingPageListener != null && this.boothViewModel != null)
			{
				this.listingPageListener.goToProfilePage(this.boothViewModel.getCompany().getCompanyId());
			}
		}
	}

	/**********************************************************************************************
	 * Booth Data Changed Callbacks
	 **********************************************************************************************/
	@Override
	public void onBoothDataLoaded()
	{
		// TODO Allow this to update colors maybe?
	}

	@Override
	public void onCompanyDataLoaded()
	{
		updateBooth();
	}

	@Override
	public void onBoothDataUpdated()
	{
		updateBooth();
	}

	/**********************************************************************************************
	 * Booth Implements
	 **********************************************************************************************/
	@Override
	public void updateBooth()
	{
		if (this.boothDataListener != null)
		{
			this.boothViewModel = this.boothDataListener.getBoothViewModel(getArguments().getInt(BOOTH_ID_KEY));
			if (this.boothViewModel != null)
			{
				this.viewProfileButton.setOnClickListener(this);

				CompanyModel company = this.boothViewModel.getCompany();
				BoothModel booth = this.boothViewModel.getBooth();
				
				int color = booth.getColor(getActivity().getResources());
				
				GradientDrawable buttonBg = (GradientDrawable) this.viewProfileButton.getBackground();
				buttonBg.setColor(color);

				this.logo.setImageBitmap(company.getCompanyLogo());
				
				// TODO: Make a circle and color it properly

				this.header.setText(company.getCompanyName());

				this.subHeader.setTextColor(color);
				this.subHeader.setText(BOOTH_PREFIX + booth.getBoothNumber());

				if (getArguments().getBoolean(IS_BIG_KEY))
				{
					this.details.setText(company.getCompanyDetails());
				}
				else
				{
					this.details.setVisibility(View.GONE);
				}
			}
		}
	}
}
