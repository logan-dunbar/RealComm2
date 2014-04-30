package com.openbox.realcomm2.utilities.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.database.models.BoothModel;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.database.models.CompanyModel;
import com.openbox.realcomm2.utilities.enums.AppMode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class BoothListAdapter extends ArrayAdapter<BoothViewModel> implements Filterable
{
	private LayoutInflater layoutInflater;
	private Resources resources;

	private List<BoothViewModel> fullBoothList = new ArrayList<BoothViewModel>();
	private AppMode currentAppMode;
	
	private String filter;

	public void setCurrentAppMode(AppMode currentAppMode)
	{
		this.currentAppMode = currentAppMode;
	}

	public BoothListAdapter(Context context)
	{
		super(context, 0);
		this.resources = context.getResources();
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setItems(List<BoothViewModel> boothList)
	{
		if (boothList != null)
		{
			this.fullBoothList = boothList;
		}
		
		doFiltering();
	}

	public void filterItems(String filter)
	{
		this.filter = filter;
		doFiltering();
	}
	
	private void doFiltering()
	{
		clear();
		
		if (this.filter == null || this.filter.length() == 0)
		{
			addAll(this.fullBoothList);
		}
		else
		{
			// Perform filtering
			List<BoothViewModel> filteredBoothList = new ArrayList<BoothViewModel>();
			for (int i = 0; i < this.fullBoothList.size(); i++)
			{
				BoothViewModel viewModel = this.fullBoothList.get(i);
				CompanyModel company = viewModel.getCompany();
				BoothModel booth = viewModel.getBooth();
				if (company.getCompanyName().toUpperCase(Locale.ENGLISH).contains(this.filter.toString().toUpperCase(Locale.ENGLISH)) ||
					String.valueOf(booth.getBoothNumber()).contains(this.filter.toString()))
				{
					filteredBoothList.add(viewModel);
				}
			}
			
			addAll(filteredBoothList);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row;
		if (null == convertView)
		{
			row = this.layoutInflater.inflate(R.layout.booth_list_item, null);
		}
		else
		{
			row = convertView;
		}

		BoothViewModel viewModel = getItem(position);
		BoothModel booth = viewModel.getBooth();
		CompanyModel company = viewModel.getCompany();

		TextView companyNameTextView = (TextView) row.findViewById(R.id.companyNameTextView);
		companyNameTextView.setText(company.getCompanyName());

		// Get left drawable
		GradientDrawable circle = (GradientDrawable) companyNameTextView.getCompoundDrawables()[0];
		if (this.currentAppMode == AppMode.Offline)
		{
			circle.setColor(this.resources.getColor(R.color.opaque_light_grey));
		}
		else
		{
			circle.setColor(booth.getColor(this.resources));
		}

		// TODO: Implement holder pattern

		return row;
	}

	@Override
	public Filter getFilter()
	{
		return new Filter()
		{
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				clear();
				BoothListAdapter.this.addAll((List<BoothViewModel>) results.values);
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				FilterResults results = new FilterResults();
				if (constraint == null || constraint.length() == 0)
				{
					// No filter implemented, return full list
					results.values = BoothListAdapter.this.fullBoothList;
					results.count = BoothListAdapter.this.fullBoothList.size();
				}
				else
				{
					// Perform filtering
					List<BoothViewModel> filteredBoothList = new ArrayList<BoothViewModel>();
					for (int i = 0; i < BoothListAdapter.this.fullBoothList.size(); i++)
					{
						BoothViewModel viewModel = BoothListAdapter.this.fullBoothList.get(i);
						CompanyModel company = viewModel.getCompany();
						BoothModel booth = viewModel.getBooth();
						if (company.getCompanyName().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase(Locale.ENGLISH)) ||
							String.valueOf(booth.getBoothNumber()).contains(constraint.toString()))
						{
							filteredBoothList.add(viewModel);
						}
					}

					results.values = filteredBoothList;
					results.count = filteredBoothList.size();
				}

				return results;
			}
		};
	}
}
