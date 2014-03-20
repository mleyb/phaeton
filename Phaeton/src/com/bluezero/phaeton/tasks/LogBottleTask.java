package com.bluezero.phaeton.tasks;

import android.content.Context;
import com.bluezero.phaeton.models.Bottle;
import com.bluezero.phaeton.utils.*;

public class LogBottleTask extends PostNewChildDataTask<Bottle, String, AsyncTaskResult<Void>> {
	public LogBottleTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Bottle... params) {			
		Bottle bottle = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("bottle", bottle);
		
		return result;	    	    
	}
}
