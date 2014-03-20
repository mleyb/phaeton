package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Nappy;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogNappyTask extends PostNewChildDataTask<Nappy, String, AsyncTaskResult<Void>> {
	public LogNappyTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Nappy... params) {			
		Nappy nappy = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("nappy", nappy);
    		
		return result;	    	    
	}
}
