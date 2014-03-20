package com.bluezero.phaeton;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bluezero.phaeton.services.air.HttpAuthenticationException;
import com.bluezero.phaeton.services.air.IAirService;
import com.bluezero.phaeton.utils.UriFormatter;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static String _registrationId;


	public static String getRegistrationId() {
	    return _registrationId;
	}

	public GCMIntentService(){
	    super(Constants.GCM_SENDER_ID);
	}
	
	@Override
	protected void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(Constants.GCM.GCM_RECEIVED_ACTION);		
		broadcastIntent.putExtra("message", intent.getStringExtra("message"));
		 
		context.sendBroadcast(broadcastIntent);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		_registrationId = registrationId;
		
		IAirService air = ((PhaetonApplication)context.getApplicationContext()).getAirService();
		
		try {
			air.httpPost(SessionInfo.Token, UriFormatter.formatUriForResource(Constants.Air.GCM_REGISTRATION_RESOURCE_URI, _registrationId));
		} 
		catch (HttpAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		IAirService air = ((PhaetonApplication)context.getApplicationContext()).getAirService();
		
		try {
			air.httpDelete(SessionInfo.Token, UriFormatter.formatUriForResource(Constants.Air.GCM_REGISTRATION_RESOURCE_URI, _registrationId));
		} 
		catch (HttpAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
