package com.bluezero.phaeton.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.Intent;

public class CalendarUtil {
	public static void addBirthdayEvent(Context context, int day, int month, int year, String name) {
		GregorianCalendar start = new GregorianCalendar(year, month, day);    		
		GregorianCalendar end = new GregorianCalendar(year + 5, month, day);
		end.add(Calendar.DAY_OF_MONTH, 1);
		
		Intent intent = new Intent(Intent.ACTION_INSERT);		
		intent.setType("vnd.android.cursor.item/event");
		
		intent.putExtra("beginTime", start.getTimeInMillis());
		intent.putExtra("endTime", end.getTimeInMillis());
		intent.putExtra("allDay", true);
		intent.putExtra("rrule", "FREQ=YEARLY;");	
		intent.putExtra("title", name + " Birthday");
		intent.putExtra("hasAlarm", 1);
		
		context.startActivity(intent);
	}
	
	public static void addBirthdayEvent(Context context, Calendar start, int yearsTillEnd, String name) {
		Calendar end = Calendar.getInstance();
		end.clear();
		end.set(start.get(Calendar.YEAR) + yearsTillEnd, start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH), 7, 0);
		end.roll(Calendar.DAY_OF_MONTH, true);
		
	    Intent intent = new Intent(Intent.ACTION_INSERT);
	    intent.setType("vnd.android.cursor.item/event");
	    intent.putExtra("beginTime", start.getTimeInMillis());
	    intent.putExtra("endTime", end.getTimeInMillis());
	    intent.putExtra("allDay", true);
		intent.putExtra("rrule", "FREQ=YEARLY;");	
		intent.putExtra("title", name + " Birthday");
		intent.putExtra("hasAlarm", 1);
	    intent.putExtra("allDay", true);
	    context.startActivity(intent);
	}
}
