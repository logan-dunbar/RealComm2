package com.openbox.realcomm2.utilities.helpers;

import com.openbox.realcomm2.utilities.interfaces.ClearFocusInterface;

import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

public class ClearFocusTouchListener implements OnTouchListener
{
	private ClearFocusInterface clearFocusListener;
	private Context context;
	
	public ClearFocusTouchListener(Context context, ClearFocusInterface listener)
	{
		this.clearFocusListener = listener;
		this.context = context;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN && this.clearFocusListener != null)
		{
			int x = (int) event.getRawX();
			int y = (int) event.getRawY();
			IBinder windowToken = v.getWindowToken();
			
			View viewToClearFocus = this.clearFocusListener.getViewToClearFocus();
			if (viewToClearFocus != null && viewToClearFocus.hasFocus())
			{
				Rect outRect = new Rect();
				viewToClearFocus.getGlobalVisibleRect(outRect);
				if (!outRect.contains(x, y))
				{
					viewToClearFocus.clearFocus();
					InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
					
					// TODO: might need to change this flag
					imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		
		return false;
	}
}
