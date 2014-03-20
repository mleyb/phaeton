package com.bluezero.phaeton;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.*;
import com.google.android.gms.common.*;

public class GoogleAuthenticator {	
	private static final String TAG = GoogleAuthenticator.class.getSimpleName();
	
	public static final String SCOPE = "oauth2: https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
			   	
	public static void refreshAccessToken(final Context context) {	  
		if (SessionInfo.Token != null) {
			invalidateToken(context);
		}
		
		int attempts = 1;
		
		while (attempts != 3) {
			try {
				SessionInfo.Token = GoogleAuthUtil.getToken(context, SessionInfo.AccountName, SCOPE);
				break;
			} 
			catch (UserRecoverableAuthException recoverableException) {
				Log.e(TAG, "Recoverable authentication exception: " + recoverableException.getMessage(), recoverableException);
				break;
			} 
			catch (GoogleAuthException authEx) {
				Log.e(TAG, "Unrecoverable authentication exception: " + authEx.getMessage(), authEx);
				break;
			} 
			catch (IOException ioEx) {
				Log.i(TAG, "Transient error encountered: " + ioEx.getMessage());
				// pause before retry
				pause();
			}		
			
			attempts++;
		}
	}
	
	public static void refreshAccessTokenInteractive(final Activity activity) {
		int attempts = 1;
		
		while (attempts != 3) {						
			try {
				SessionInfo.Token = GoogleAuthUtil.getToken(activity, SessionInfo.AccountName, SCOPE);				
				break;
			} 
			catch (GooglePlayServicesAvailabilityException playEx) {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(playEx.getConnectionStatusCode(), activity, Constants.RequestCodes.LAUNCH_AUTH_INTENT);
				dialog.show();
				break;
			} 
			catch (UserRecoverableAuthException recoverableException) {
				Intent recoveryIntent = recoverableException.getIntent();
				activity.startActivityForResult(recoveryIntent, Constants.RequestCodes.LAUNCH_AUTH_INTENT);
				break;
			} 
			catch (GoogleAuthException authEx) {
				Log.e(TAG, "Unrecoverable authentication exception: " + authEx.getMessage(), authEx);
				break;
			} 
			catch (IOException ioEx) {
				Log.i(TAG, "Transient error encountered: " + ioEx.getMessage());
				// pause before retry
				pause();				
			}		
			
			attempts++;
		}
	}
	
	private static void invalidateToken(final Context context) {
		Log.d(TAG, "Invalidating old token...");
		GoogleAuthUtil.invalidateToken(context, SessionInfo.Token);
		SessionInfo.Token = null;
		Log.d(TAG, "Old token invalidated OK.");
	}
	
	private static void pause() {
		try { 
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) { 
			Log.e(TAG, "Exception during thread sleep: " + e.toString(), e);
		}		
	}
}
