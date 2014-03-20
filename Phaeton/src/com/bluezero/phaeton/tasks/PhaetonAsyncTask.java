package com.bluezero.phaeton.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class PhaetonAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private final boolean _showProgress;
	private ProgressDialog _progress;
	
	protected final Context _context;
	
	public PhaetonAsyncTask(Context context, boolean showProgress) {
		_context = context;
		_showProgress = showProgress;
	}
		
	@Override
    protected void onPreExecute() {
		if (_showProgress) { 
			_progress = new ProgressDialog(_context);
			_progress.setTitle("Please wait");
			_progress.setMessage(getProgressMessage());
			_progress.setIndeterminateDrawable(_context.getResources().getDrawable(com.bluezero.phaeton.R.drawable.progress));
			_progress.setIcon(android.R.drawable.ic_dialog_info);
			_progress.show();						
		}
	}
	
	@Override
	protected void onProgressUpdate(Progress... progress) {
        if (_showProgress) {
        	_progress.setMessage((String)progress[0]);
        }
    }
	
	@Override
	protected void onPostExecute(Result result) {
		if (_showProgress) {
			_progress.dismiss();
		}	
	}
		
	protected abstract String getProgressMessage();
}
