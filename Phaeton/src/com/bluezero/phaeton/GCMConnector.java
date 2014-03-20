package com.bluezero.phaeton;

import android.app.Activity;

import com.google.android.gcm.GCMRegistrar;

public class GCMConnector {
	private String _gcmRegistrationId;
	
	public void register(Activity activity) {
    	GCMRegistrar.checkDevice(activity);
	    GCMRegistrar.checkManifest(activity);
	    
	    _gcmRegistrationId = GCMRegistrar.getRegistrationId(activity);
	    
	    if (_gcmRegistrationId.equals("")) {
	        GCMRegistrar.register(activity, Constants.GCM_SENDER_ID);
	    }
    }
	
	public void unregister(Activity activity) {
		GCMRegistrar.onDestroy(activity);
	}
}
