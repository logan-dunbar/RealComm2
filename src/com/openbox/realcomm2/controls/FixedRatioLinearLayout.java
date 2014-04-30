package com.openbox.realcomm2.controls;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FixedRatioLinearLayout extends LinearLayout
{
	public FixedRatioLinearLayout(Context context)
	{
		super(context);
	}

	public FixedRatioLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FixedRatioLinearLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO could probably use attributes and set aspects in XML as well
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
		int originalHeight = MeasureSpec.getSize(heightMeasureSpec);

		int finalWidth = originalWidth;
		int finalHeight = originalHeight;

		Drawable background = getBackground();
		if (background != null)
		{
			int bgWidth = background.getIntrinsicWidth();
			int bgHeight = background.getIntrinsicHeight();

			double aspectRatio = (double) bgWidth / (double) bgHeight;

			int calculatedHeight = (int) (finalWidth / aspectRatio);

			if (calculatedHeight > originalHeight)
			{
				// If calculated height is bigger, we need to scale width down
				finalWidth = (int) (originalHeight * aspectRatio);
			}
			else
			{
				// else scale height down
				finalHeight = calculatedHeight;
			}
		}

		super.onMeasure(
			MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
			MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
	}
}
