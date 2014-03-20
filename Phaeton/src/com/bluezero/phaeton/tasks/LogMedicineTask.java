package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Medicine;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogMedicineTask extends PostNewChildDataTask<Medicine, String, AsyncTaskResult<Void>> {
	public LogMedicineTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Medicine... params) {			
		Medicine medicine = params[0];

		AsyncTaskResult<Void> result = super.postDataWithRetry("medicine", medicine);
    		
    	return result;	    	    
	}
}
