package com.bluezero.phaeton.widgets;

import java.util.Calendar;
import java.util.Date;

public class DateTimeSelection
{
	public int Minute;
	public int Hour;			
	public int Day;
	public int Year;
	public int Month;
	
	public static DateTimeSelection getForCurrentDate() {
		DateTimeSelection dateSelection = new DateTimeSelection();
		Calendar calendar = Calendar.getInstance();
		dateSelection.Year = calendar.get(Calendar.YEAR);
		dateSelection.Month = calendar.get(Calendar.MONTH) + 1; // 0 based
		dateSelection.Day = calendar.get(Calendar.DAY_OF_MONTH);
		dateSelection.Minute = calendar.get(Calendar.MINUTE);
		dateSelection.Hour = calendar.get(Calendar.HOUR_OF_DAY);
		return dateSelection;
	}
	
	public String toDateString() {
		return Day + "/" + Month + "/" + Year;
	}
	
	public String toTimeString() {
		return Hour + ":" + Minute;
	}
	
	public Date toDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Year, Month, Day, Hour, Minute, 0);
		Date date = calendar.getTime();
		return date;
	}
}
