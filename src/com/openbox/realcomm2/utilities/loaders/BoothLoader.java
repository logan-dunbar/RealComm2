package com.openbox.realcomm2.utilities.loaders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.openbox.realcomm2.database.DatabaseManager;
import com.openbox.realcomm2.database.models.BoothModel;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.database.objects.Booth;

public class BoothLoader extends AsyncTaskLoader<List<BoothViewModel>>
{
	private List<BoothViewModel> boothViewModelList;

	public BoothLoader(Context context)
	{
		super(context);
	}

	@Override
	protected void onStartLoading()
	{
		// If we currently have a result available, deliver it immediately.
		if (this.boothViewModelList != null)
		{
			deliverResult(this.boothViewModelList);
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

		if (this.boothViewModelList != null)
		{
			this.boothViewModelList = null;
		}
	}

	@Override
	public void deliverResult(List<BoothViewModel> data)
	{
		this.boothViewModelList = data;

		// If the Loader is currently started, we can immediately deliver its results.
		if (isStarted())
		{
			super.deliverResult(data);
		}
	}

	@Override
	public List<BoothViewModel> loadInBackground()
	{
		List<BoothViewModel> boothViewModelArray = new ArrayList<BoothViewModel>();

		try
		{
			List<Booth> boothList = DatabaseManager.getInstance().getAll(Booth.class);

			for (Booth booth : boothList)
			{
				boothViewModelArray.add(new BoothViewModel(new BoothModel(booth)));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return boothViewModelArray;
	}

}
