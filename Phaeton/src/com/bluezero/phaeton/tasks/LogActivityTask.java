package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Activity;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogActivityTask extends PostNewChildDataTask<Activity, String, AsyncTaskResult<Void>> {
	public LogActivityTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Activity... params) {
		Activity activity = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("activity", activity);
		
		return result;
	}
}
