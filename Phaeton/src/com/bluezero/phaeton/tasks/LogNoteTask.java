package com.bluezero.phaeton.tasks;

import android.content.Context;

import com.bluezero.phaeton.models.Note;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;

public class LogNoteTask extends PostNewChildDataTask<Note, String, AsyncTaskResult<Void>> {
	public LogNoteTask(Context context, IAsyncTaskResultHandler resultHandler, int childId) {
		super(context, resultHandler, childId);
	}
		
	@Override 
	protected AsyncTaskResult<Void> doInBackground(Note... params) {			
		Note note = params[0];
		   	
		AsyncTaskResult<Void> result = super.postDataWithRetry("note", note);
    		
		return result;
	}
}
