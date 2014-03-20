package com.bluezero.phaeton;

import com.bluezero.phaeton.services.air.AirService;
import com.bluezero.phaeton.services.air.IAirService;
import com.bugsense.trace.BugSenseHandler;
import com.google.analytics.tracking.android.*;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;

public class PhaetonApplication extends Application {
	private static final String TAG = PhaetonApplication.class.getSimpleName();
	
	private static PhaetonApplication _instance;
	
	private IAirService _airService;

	private ServiceConnection _airServiceConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	    	_airService = (IAirService)(((AirService.AirServiceBinder)service).getService());
	    }

	    public void onServiceDisconnected(ComponentName className) {
	    	_airService = null;
	    }
	};

	private OnSharedPreferenceChangeListener _prefsListener = new OnSharedPreferenceChangeListener(){
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals("enableGoogleAnalytics")) {
				boolean enabled = sharedPreferences.getBoolean("enableGoogleAnalytics", true);
				GoogleAnalytics ga = GoogleAnalytics.getInstance(getApplicationContext());
				ga.setAppOptOut(enabled);		        
			}
		}
	};	
	
	public static PhaetonApplication getInstance() {		
		return _instance;
	}	
	
	public IAirService getAirService() {
		return _airService;
	}
		
	@Override
	public void onCreate() {
		super.onCreate();			

		Log.i(TAG, "onCreate");
		
    	BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_API_KEY);
		
		GoogleAnalytics.getInstance(this).setDebug(BuildConfig.DEBUG);   	
		
		_instance = this;					
	
		// attach the service
		bindService(new Intent(PhaetonApplication.this, AirService.class), _airServiceConnection, Context.BIND_AUTO_CREATE);
		
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(_prefsListener);		
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		Log.i(TAG, "onTerminate");
		
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(_prefsListener);
		
		// detach the service
		unbindService(_airServiceConnection);
		
		BugSenseHandler.closeSession(this);
	}

	@Override
	public void onLowMemory() {
		// In-memory caches should be thrown overboard here
		Log.i(TAG, "onLowMemory");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, "onConfigurationChanged");

		if (Log.isLoggable(TAG, Log.VERBOSE)) {
			Log.v(TAG, newConfig.toString());
		}
	}	
	
	public void confirmQuit(Context context) {
    	ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Phaeton);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		builder.setTitle(R.string.app_name);
		//builder.setIcon(FontAwesomeIcon.STOP.getIconUtfValue());
		//builder.setIcon(android.R.drawable.ic_menu_help);
		builder.setMessage("Are you sure you want to quit?");
		builder.setCancelable(false);
		
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
        	   PhaetonApplication application = PhaetonApplication.getInstance();
        	   application.onTerminate();
               System.exit(0);
           }
		});
		
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
}
