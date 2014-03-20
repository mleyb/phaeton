package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.FirstAid;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogFirstAidTask extends PostNewChildDataTask<FirstAid, String, AsyncTaskResult<Void>> {
	public LogFirstAidTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(FirstAid... params) {			
		FirstAid firstAid = params[0];
   		
		AsyncTaskResult<Void> result = super.postDataWithRetry("firstaid", firstAid);
    		
    	return result;	    	    
	}
}
