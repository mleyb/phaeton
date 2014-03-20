package com.bluezero.phaeton.tasks;

import android.content.Context;
import android.util.Log;

import com.bluezero.phaeton.Constants;
import com.bluezero.phaeton.GoogleAuthenticator;
import com.bluezero.phaeton.PhaetonApplication;
import com.bluezero.phaeton.SessionInfo;
import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.services.air.HttpAuthenticationException;
import com.bluezero.phaeton.services.air.IAirService;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bugsense.trace.BugSenseHandler;

public class CreateChildTask extends PhaetonAsyncTask<Child, String, AsyncTaskResult<Child>> {
	private static final String TAG = CreateChildTask.class.getSimpleName();
	
	private final IAsyncTaskResultHandler _resultHandler;
	
	public CreateChildTask(Context context, IAsyncTaskResultHandler resultHandler, boolean showProgress) {
		super(context, showProgress);
		_resultHandler = resultHandler;		
	}
	
	@Override
	protected String getProgressMessage() {
		return "Saving information...";
	}
	
	@Override
    protected void onPreExecute() {
		super.onPreExecute();			
	}
	
	@Override 
	protected AsyncTaskResult<Child> doInBackground(Child... params) {
		AsyncTaskResult<Child> result = null;
		
		// make sure we have a good access token
		GoogleAuthenticator.refreshAccessToken(_context);				
		
		IAirService air = PhaetonApplication.getInstance().getAirService();
		
		Child newChild = params[0];	
		
		int attempts = 0;
		
		while (attempts != TaskConstants.RetryCount) {		
			try {
				attempts++;
				
				Log.d(TAG, "Attempting to send data to server");
								
				Child createdChild = air.httpPostObject(SessionInfo.Token, Constants.Air.CHILD_RESOURCE_URI, newChild, newChild.getClass());
				
				result = new AsyncTaskResult<Child>(createdChild);			
				
				Log.d(TAG, "Data sent to server successfully");
				
				break;
			}
			catch (HttpAuthenticationException authEx) {
				super.publishProgress("Retrying...");
				
				Log.e(TAG, "Retrying attempt to send data to server due to authentication failure");
				BugSenseHandler.sendException(authEx);						
			
				if (attempts == TaskConstants.RetryCount) {
					result = new AsyncTaskResult<Child>(authEx);	
				}										
				else {
					// get a fresh token for the next retry
					GoogleAuthenticator.refreshAccessToken(_context);	
				}
			}
			catch (Exception ex) {
				super.publishProgress("Retrying...");
				
				Log.e(TAG, "Retrying attempt to send data to server due to exception: " + ex.toString());
				BugSenseHandler.sendException(ex);
				
				if (attempts == TaskConstants.RetryCount) {
					result = new AsyncTaskResult<Child>(ex);	
				}
			}		
		}			
        
        return result;
	}
			
	@Override
	protected void onPostExecute(AsyncTaskResult<Child> result) {
		super.onPostExecute(result);		
		_resultHandler.handleResult(result);
	}
}
