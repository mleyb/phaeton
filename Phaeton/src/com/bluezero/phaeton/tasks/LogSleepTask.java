package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Sleep;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogSleepTask extends PostNewChildDataTask<Sleep, String, AsyncTaskResult<Void>> {
	public LogSleepTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Sleep... params) {			
		Sleep sleep = params[0];
		
		AsyncTaskResult<Void> result = super.postDataWithRetry("sleep", sleep);
    		
		return result;	    	    
	}
}
