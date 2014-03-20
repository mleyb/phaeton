package com.bluezero.phaeton.tasks;

import android.content.Context;
import android.util.Log;

import com.bluezero.phaeton.data.DataSource;
import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bugsense.trace.BugSenseHandler;

public class InsertChildToDBTask extends PhaetonAsyncTask<Child, Void, AsyncTaskResult<Void>> {
	private static final String TAG = InsertChildToDBTask.class.getSimpleName();
	
	public InsertChildToDBTask(Context context) {
		super(context, false);
	}

	@Override
	protected String getProgressMessage() {
		return null;
	}

	@Override
	protected AsyncTaskResult<Void> doInBackground(Child... params) {
		AsyncTaskResult<Void> result = null;
		
		DataSource dataSource = null;
        
        try {
        	Child child = params[0];        	

        	dataSource = new DataSource(_context);
        	dataSource.insert(child);
        	
        	result = new AsyncTaskResult<Void>();
        }
        catch (Exception ex) {
        	Log.e(TAG, "Database insert exception: " + ex.toString());
        	BugSenseHandler.sendException(ex);
        	
        	result = new AsyncTaskResult<Void>(ex);
        }
        finally {
        	dataSource.cleanup();
        }
        
        return result;
	}
}