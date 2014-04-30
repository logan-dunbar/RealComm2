package com.openbox.realcomm2.utilities.adapters;

import android.util.SparseArray;
import android.widget.BaseAdapter;

public abstract class SparseArrayAdapter<E> extends BaseAdapter
{
	// TODO: Maybe not initialize to force setting of data?
	private SparseArray<E> data = new SparseArray<E>();

	@Override
	public int getCount()
	{
		return this.data.size();
	}

	@Override
	public E getItem(int position)
	{
		return this.data.valueAt(position);
	}

	@Override
	public long getItemId(int position)
	{
		return this.data.keyAt(position);
	}

	protected void addAll(SparseArray<E> data)
	{
		this.data = data;
		notifyDataSetChanged();
	}
}
