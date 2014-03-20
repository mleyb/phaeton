package com.bluezero.phaeton.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

public final class ISO8601 {
    public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }       
    
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    public static Calendar toCalendar(final String iso8601String) throws Exception {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601String.replace("Z", "+00:00");
        
        try {
            s = s.substring(0, 22) + s.substring(23);
        } 
        catch (IndexOutOfBoundsException e) {
        	Log.e("IS08601", e.toString());            
        }
        
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        
        return calendar;
    }
    
    public static String toSimpleDateFormattedString(final String iso8601String) {
    	String dateString = null;
		try {
			Calendar calendar = toCalendar(iso8601String);
			dateString = DateTimeUtil.toSimpleDateFormattedString(calendar.getTime());
			
		} catch (Exception e) {
			Log.e("IS08601", e.toString());
		}
    	
		return dateString;
    }
    
    public static Date parseISO8601String(String iso8601DateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date result = null;
		try {
			result = df.parse(iso8601DateString);
		} catch (ParseException e) {
			Log.e("IS08601", e.toString());
		}
        return result;        
    }
}
