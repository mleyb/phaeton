package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Sick;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogSickTask extends PostNewChildDataTask<Sick, String, AsyncTaskResult<Void>> {
	public LogSickTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Sick... params) {			
		Sick sick = params[0];
		   		
		AsyncTaskResult<Void> result = super.postDataWithRetry("sick", sick);
    		
		return result;    		    	   
	}
}
