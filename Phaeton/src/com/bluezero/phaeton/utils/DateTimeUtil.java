package com.bluezero.phaeton.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;
import android.widget.DatePicker;

public final class DateTimeUtil {
	public static Calendar GetLocalCalendar() {
		return Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
	}
	
	public static String getCurrentDateJSONString() {
		return toUTCFormattedString(new Date());
	}	
	
	public static Date getCurrentDate(DatePicker datePicker) {
		return new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
	}
	
	public static Date parseSimpleDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/yy", Locale.UK);
		Date date = null;
		try {
			date = (Date)formatter.parse(dateString);
		} 
		catch (ParseException e) {
			Log.e(DateTimeUtil.class.getSimpleName(), "Failed to parse date");
			e.printStackTrace();
		} 
	  	return date;
	}
	
	public static String toUTCFormattedString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
	}
	
	public static String toSimpleDateFormattedString(Date date) {
		try {        
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	return sdf.format(date);
        } 
        catch (Exception e) {        	
        }
		
		return null;
	}
}
