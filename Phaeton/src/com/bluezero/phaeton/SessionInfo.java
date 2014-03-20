package com.bluezero.phaeton;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionInfo {
	public static String CarerId;
	public static String Key;
	public static String Token;
	public static String DisplayName;	
	public static String AccountName;
	public static boolean IsProfessional;
	
	public static void restore(Context context) {		
		SecurePreferences prefs = new SecurePreferences(context);					
		CarerId = prefs.getString("carerId", null);
		Key = prefs.getString("key", null);
		Token = prefs.getString("token", null);
		DisplayName = prefs.getString("displayName", null);
		AccountName = prefs.getString("accountName", null);
		IsProfessional = prefs.getBoolean("isProfessional", false);
	}
	
	public static void save(Context context) {
		SecurePreferences prefs = new SecurePreferences(context);
		SharedPreferences.Editor editor = prefs.edit();		
		editor.putString("carerId", CarerId);		
		editor.putString("key", Key);		
		editor.putString("token", Token);
		editor.putString("displayName", DisplayName);
		editor.putString("accountName", AccountName);
		editor.putBoolean("isProfessional", IsProfessional);
		editor.commit();
	}
	
	public static void clear() {
		CarerId = null;		
		Token = null;
		Key = null;
		DisplayName = null;
		AccountName = null;
		IsProfessional = false;
	}
}
