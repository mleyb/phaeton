package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Drink;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogDrinkTask extends PostNewChildDataTask<Drink, String, AsyncTaskResult<Void>> {
	public LogDrinkTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Drink... params) {			
		Drink drink = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("drink", drink);
		
		return result;	    	    
	}
}
