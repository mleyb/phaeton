package com.bluezero.phaeton.tasks;

import android.content.Context;
import android.util.Log;

import com.bluezero.phaeton.Constants;
import com.bluezero.phaeton.GoogleAuthenticator;
import com.bluezero.phaeton.PhaetonApplication;
import com.bluezero.phaeton.SessionInfo;
import com.bluezero.phaeton.services.air.HttpAuthenticationException;
import com.bluezero.phaeton.services.air.IAirService;
import com.bluezero.phaeton.utils.*;
import com.bugsense.trace.BugSenseHandler;

public abstract class PostNewChildDataTask<Params, Progress, Result> extends PhaetonAsyncTask<Params, String, Result> {
	private static final String TAG = PostNewChildDataTask.class.getSimpleName();
	
	private final IAsyncTaskResultHandler _resultHandler;
	private final int _childId;
	
	public PostNewChildDataTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, true);
		_resultHandler = resultHandler;		
		_childId = childId;
	}
	
	protected IAsyncTaskResultHandler getResultHandler() {
		return _resultHandler;
	}
	
	protected int getChildId() { 
		return _childId;
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
	@SuppressWarnings("unchecked")
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);        
		_resultHandler.handleResult((AsyncTaskResult<Result>)result);
	}
	
	protected AsyncTaskResult<Void> postDataWithRetry(String subResource, Object content) {		
		AsyncTaskResult<Void> result = null;
		
		// make sure we have a good access token
		GoogleAuthenticator.refreshAccessToken(_context);				
		
		String uri = UriFormatter.formatUriForResource(Constants.Air.CHILD_RESOURCE_URI, _childId, subResource);
		
		IAirService air = PhaetonApplication.getInstance().getAirService();
		
		int attempts = 1;
		
		while (attempts != TaskConstants.RetryCount) {		
			try {
				attempts++;
				
				Log.d(TAG, "Attempting to send data to server");
								
				air.httpPostObject(SessionInfo.Token, uri, content, content.getClass());
				
				Log.d(TAG, "Data sent to server successfully");
				
				result = new AsyncTaskResult<Void>();
				
				break;
			}
			catch (HttpAuthenticationException authEx) {
				super.publishProgress("Retrying...");
				
				Log.e(TAG, "Retrying attempt to send data to server due to authentication failure");
				BugSenseHandler.sendException(authEx);
				
				if (attempts == TaskConstants.RetryCount) {
					result = new AsyncTaskResult<Void>(authEx);	
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
					result = new AsyncTaskResult<Void>(ex);	
				}													
			}					
		}
		
		return result;
   	}
}