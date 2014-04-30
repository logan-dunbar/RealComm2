package com.openbox.realcomm2.utilities.helpers;

import java.util.Calendar;
import java.util.Date;

public class DateHelper
{
	private DateHelper() {}
	
	public static Date getDateOnly(Date date)
	{
		if (date == null)
		{
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}

}
