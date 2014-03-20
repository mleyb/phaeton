package com.bluezero.phaeton.tasks;

import android.content.Context;
import android.util.Log;

import com.bluezero.phaeton.Constants;
import com.bluezero.phaeton.GoogleAuthenticator;
import com.bluezero.phaeton.PhaetonApplication;
import com.bluezero.phaeton.SessionInfo;
import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.UriFormatter;
import com.bugsense.trace.BugSenseHandler;

public class DeleteChildTask extends PhaetonAsyncTask<Child, Void, AsyncTaskResult<Child>> {
	private static final String TAG = DeleteChildTask.class.getSimpleName();
	
	private final IAsyncTaskResultHandler _resultHandler;
	
	public DeleteChildTask(Context context, IAsyncTaskResultHandler resultHandler) {
		super(context, true);
		_resultHandler = resultHandler;
	}
	
	@Override
	protected String getProgressMessage() {
		return "Deleting...";
	}
	
	@Override
    protected void onPreExecute() {
		super.onPreExecute();		
	}
	
	@Override 
	protected AsyncTaskResult<Child> doInBackground(Child... params) {
		// make sure we have a good access token
		GoogleAuthenticator.refreshAccessToken(_context);
		
		Child child = params[0];

		try {   		
			String uri = UriFormatter.formatUriForResource(Constants.Air.CHILD_RESOURCE_URI, child.Id);
				
			PhaetonApplication.getInstance().getAirService().httpDelete(SessionInfo.Token, uri);
    		
    		return new AsyncTaskResult<Child>(child);
		}
    	catch (Exception e) {
    		Log.e(TAG, "There was an error", e);
    		BugSenseHandler.sendException(e);
    		return new AsyncTaskResult<Child>(e);
    	}	    	    
	}
	
	@Override
	protected void onPostExecute(AsyncTaskResult<Child> result) {        	        
        super.onPostExecute(result);
        _resultHandler.handleResult(result);
	}				
}
