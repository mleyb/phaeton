package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Milestone;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogMilestoneTask extends PostNewChildDataTask<Milestone, String, AsyncTaskResult<Void>> {
	public LogMilestoneTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Milestone... params) {			
		Milestone milestone = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("milestone", milestone);
    		
		return result;
	}
}
