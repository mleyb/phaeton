package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Snack;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogSnackTask extends PostNewChildDataTask<Snack, String, AsyncTaskResult<Void>> {
	public LogSnackTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Snack... params) {			
		Snack snack = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("snack", snack);
    		
		return result;	    	    
	}
}
