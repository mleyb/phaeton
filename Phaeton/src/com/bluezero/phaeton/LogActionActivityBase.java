package com.bluezero.phaeton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.services.air.HttpAuthenticationException;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.IntentUtil;
import com.bluezero.phaeton.utils.ViewUtil;
import com.bugsense.trace.BugSenseHandler;

public abstract class LogActionActivityBase extends PhaetonActivity implements IAsyncTaskResultHandler {
	private static final String TAG = LogActionActivityBase.class.getSimpleName();
	
	private Child _child;
	
	protected Child getChild() {
		return _child;
	}
		
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);               
        _child = IntentUtil.getChildFromIntent(getIntent());
    }
	
	@Override
	protected void setHeaderText(int textViewResourceId, int stringResourceId) {
		TextView headerTextView = (TextView)findViewById(textViewResourceId);
	    headerTextView.setText(getString(stringResourceId) + " for " + getChild().Forename);	
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.log_action_menu, menu);
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
    		// app icon in action bar clicked; go to select action activity
    		Intent intent = IntentUtil.generateIntent(this, SelectActionActivity.class, getChild());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
    	}
    	else {
    		return super.onOptionsItemSelected(item);
    	}
    }	       
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);

	    Button button = (Button)findViewById(R.id.confirmButton);
	    if (button != null) {	    		    
	    	super.applyButtonImageWithTextOffset(button, button.getWidth(), R.drawable.tick_button);
	    }
	}
	
	public void confirmClick(View view) {
		if (super.isNetworkConnectionAvailable()) {	
			saveData();
		}
		else {
			super.showNetworkConnectionUnavailableToast();
		}    	
    }
	
	protected abstract void saveData();
		
	public <TResult> void handleResult(AsyncTaskResult<TResult> result) {
		if (result.getError() != null) {
			if (result.getError() instanceof HttpAuthenticationException) {	
				Log.e(TAG, "Authentication failed!");
				BugSenseHandler.sendException(result.getError());
			}
			
			ViewUtil.showErrorDialog(this, Constants.Errors.GENERAL);
		}
		else {	        
			ViewUtil.showToast(this, "Saved OK");
			finish();
		}
	}	
}
