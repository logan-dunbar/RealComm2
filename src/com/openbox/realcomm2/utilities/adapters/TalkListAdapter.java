package com.openbox.realcomm2.utilities.adapters;

import java.util.List;

import com.openbox.realcomm2.R;
import com.openbox.realcomm2.database.models.TalkModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TalkListAdapter extends ArrayAdapter<TalkModel>
{
	private LayoutInflater layoutInflater;

	public TalkListAdapter(Context context)
	{
		super(context, 0);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setItems(List<TalkModel> talkList)
	{
		clear();
		if (talkList != null)
		{
			addAll(talkList);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row;
		if (null == convertView)
		{
			row = this.layoutInflater.inflate(R.layout.talk_list_item, null);
		}
		else
		{
			row = convertView;
		}

		TalkModel talk = getItem(position);

		TextView talkNameTextView = (TextView) row.findViewById(R.id.talkName);

		talkNameTextView.setText(talk.getName());

		// TODO: Implement holder pattern

		return row;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return false;
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}
}
