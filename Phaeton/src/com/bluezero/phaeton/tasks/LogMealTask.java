package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Meal;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogMealTask extends PostNewChildDataTask<Meal, String, AsyncTaskResult<Void>> {
	public LogMealTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Meal... params) {			
		Meal meal = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("meal", meal);
    		
    	return result;	    	    
	}
}
