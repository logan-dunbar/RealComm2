package com.openbox.realcomm2.controls;

import com.openbox.realcomm2.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class ClearableEditText extends EditText implements OnFocusChangeListener
{
	private static final int EXTRA_TOUCH_SIZE = 10;

	private Boolean clearable;

	private Drawable rightDrawable;
	private Rect touchableBounds;
	
	private Boolean getIsClearShowing()
	{
		Drawable[] drawables = getCompoundDrawables();
		return drawables[2] != null;
	}

	public ClearableEditText(Context context)
	{
		super(context);
		init(context);
	}

	public ClearableEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initAttrs(context, attrs);
		init(context);
	}

	public ClearableEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initAttrs(context, attrs);
		init(context);
	}

	private void initAttrs(Context context, AttributeSet attrs)
	{
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ClearableEditText, 0, 0);

		try
		{
			this.clearable = a.getBoolean(R.styleable.ClearableEditText_clearable, true);
		}
		finally
		{
			a.recycle();
		}
	}

	private void init(Context context)
	{
		this.touchableBounds = new Rect();

		if (this.clearable != null && this.clearable)
		{
			Resources resources = context.getResources();
			this.rightDrawable = resources.getDrawable(R.drawable.icon_clear_small);
			this.rightDrawable.setBounds(0, 0, this.rightDrawable.getIntrinsicWidth(), this.rightDrawable.getIntrinsicHeight());

			setOnFocusChangeListener(this);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		if (this.rightDrawable != null)
		{
			generateTouchableBounds();
		}
	}

	private void generateTouchableBounds()
	{
		if (this.rightDrawable != null)
		{
			// Make a bit bigger for fat fingers
			int extendedWidth = this.rightDrawable.getIntrinsicWidth() + EXTRA_TOUCH_SIZE;
			int extendedHeight = this.rightDrawable.getIntrinsicHeight() + EXTRA_TOUCH_SIZE;

			this.touchableBounds.left = getWidth() - (getPaddingRight() + extendedWidth);
			this.touchableBounds.right = getWidth();// - getPaddingRight();
			this.touchableBounds.top = (getHeight() - getPaddingTop() - extendedHeight) / 2;
			this.touchableBounds.bottom = this.touchableBounds.top + extendedHeight;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int actionX = (int) event.getX();
			int actionY = (int) event.getY();

			if (this.rightDrawable != null && getIsClearShowing() && this.touchableBounds.contains(actionX, actionY))
			{
				// Clear text
				setText(null);
				
				// If clearing from outside, hide the clear button
				if (!hasFocus())
				{
					hideClear();
				}

				// Stop propagation, we have done what we wanted to
				return false;
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
		if (hasFocus)
		{
			showClear();
		}
		else
		{
			if (getText().length() > 0)
			{
				showClear();
			}
			else
			{
				hideClear();
			}
		}
	}

	private void hideClear()
	{
		Drawable[] drawables = getCompoundDrawables();
		setCompoundDrawables(drawables[0], drawables[1], null, drawables[3]);
	}

	private void showClear()
	{
		Drawable[] drawables = getCompoundDrawables();
		setCompoundDrawables(drawables[0], drawables[1], this.rightDrawable, drawables[3]);
	}
}
