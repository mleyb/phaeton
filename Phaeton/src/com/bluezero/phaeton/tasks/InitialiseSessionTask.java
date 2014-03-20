package com.bluezero.phaeton.tasks;

import android.content.Context;
import android.util.Log;

import com.bluezero.phaeton.Constants;
import com.bluezero.phaeton.PhaetonApplication;
import com.bluezero.phaeton.SessionInfo;
import com.bluezero.phaeton.data.DataSource;
import com.bluezero.phaeton.models.Carer;
import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bugsense.trace.BugSenseHandler;

public class InitialiseSessionTask extends PhaetonAsyncTask<Void, Void, AsyncTaskResult<Carer>> {
	private static final String TAG = InitialiseSessionTask.class.getSimpleName();
	
	private final IAsyncTaskResultHandler _resultHandler;
	
	public InitialiseSessionTask(Context context, IAsyncTaskResultHandler resultHandler) {
		super(context, false);
		_resultHandler = resultHandler;		
	}

	@Override
	protected String getProgressMessage() {
		return "Signing in...";
	}
	
	@Override
	protected AsyncTaskResult<Carer> doInBackground(Void... params) {
		try {
			Carer carer = PhaetonApplication.getInstance().getAirService().httpGetObject(SessionInfo.Token, Constants.Air.CARER_RESOURCE_URI, Carer.class);		        		
	        		        	        	        	       
	        if (carer.Children != null && carer.Children.length > 0) {						
		        DataSource dataSource = new DataSource(_context);	               
		        
		        try {   
		        	// add all child records to the local db
	        		for (Child c : carer.Children) {        		
	        			dataSource.insert(c);
		        	}	        	
		        }
		        catch (Exception ex) {
		        	Log.e(TAG, "Sign-in exception: " + ex.toString());
		        	BugSenseHandler.sendException(ex);
		        }
		        finally {
		        	dataSource.cleanup();
		        }	        	       	       
			}
	        
	        return new AsyncTaskResult<Carer>(carer);
		} 
		catch (Exception e) {
			Log.e(TAG, "There was an error", e);
			BugSenseHandler.sendException(e);
			return new AsyncTaskResult<Carer>(e);
		}			
	}
	
	@Override
	protected void onPostExecute(AsyncTaskResult<Carer> result) {        	        
        super.onPostExecute(result);
        _resultHandler.handleResult(result);
	}
}
