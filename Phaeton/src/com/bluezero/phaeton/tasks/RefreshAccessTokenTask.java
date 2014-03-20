package com.bluezero.phaeton.tasks;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.bluezero.phaeton.GoogleAuthenticator;
import com.bluezero.phaeton.SessionInfo;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.google.android.gms.auth.GoogleAuthUtil;

public class RefreshAccessTokenTask extends PhaetonAsyncTask<Void, Void, AsyncTaskResult<Void>> {
	private static final String TAG = RefreshAccessTokenTask.class.getSimpleName();	
	
	private final IAsyncTaskResultHandler _resultHandler;
	
	public RefreshAccessTokenTask(Context context, IAsyncTaskResultHandler resultHandler) {
		super(context, false);
		_resultHandler = resultHandler;		
	}
	
	@Override
	protected String getProgressMessage() {
		return null;
	}

	@Override
	protected AsyncTaskResult<Void> doInBackground(Void... params) {
		AsyncTaskResult<Void> result = null;	
		
		GoogleAuthUtil.invalidateToken(_context, SessionInfo.Token);
		
		int attempts = 1;
		
		while (attempts != 3) {						
			try {
				SessionInfo.Token = GoogleAuthUtil.getToken(_context, SessionInfo.AccountName, GoogleAuthenticator.SCOPE);
				
				result = new AsyncTaskResult<Void>();	
				
				break;
			} 			
			catch (IOException ioEx) {
				Log.i(TAG, "Transient error encountered: " + ioEx.getMessage());
				
				if (attempts == TaskConstants.RetryCount) {
					result = new AsyncTaskResult<Void>(ioEx);	
				}
				else {									
					// pause before retry
					pause();
				}
			}
			catch (Exception ex) {
				result = new AsyncTaskResult<Void>(ex);
				break;
			}
			
			attempts++;
		}
		
        return result;
	}
	
	@Override
	protected void onPostExecute(AsyncTaskResult<Void> result) {
		super.onPostExecute(result);		
		_resultHandler.handleResult(result);
	}
			
	private static void pause() {
		try { 
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) { 
			Log.e(TAG, "Exception during thread sleep: " + e.toString(), e);
		}		
	}
}
