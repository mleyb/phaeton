package com.bluezero.phaeton;

import roboguice.activity.RoboActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bluezero.phaeton.services.air.IAirService;
import com.bluezero.phaeton.utils.ViewUtil;
import com.google.analytics.tracking.android.EasyTracker;

public abstract class PhaetonActivity extends RoboActivity {	
	private static final String TAG = PhaetonActivity.class.getSimpleName();	
	
	protected IAirService getIsisService() {
		return ((PhaetonApplication)getApplication()).getAirService();
	}
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);	       
    	
    	Log.d(TAG, "onCreate");
    	
	    SessionInfo.restore(getApplicationContext());	    
	}    
    
    @Override
    protected void onPause() {
    	super.onPause();

    	Log.d(TAG, "onPause");
    	
    	SessionInfo.save(getApplicationContext());
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	
    	Log.d(TAG, "onResume");
    }

    @Override
    public void onStart() {
      super.onStart();
      
      Log.d(TAG, "onStart");
      
      EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
      super.onStop();
      
      Log.d(TAG, "onStop");
      
      EasyTracker.getInstance().activityStop(this);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	Log.d(TAG, "onDestroy");        
    }
    
    protected void setHeaderText(int textViewResourceId, int stringResourceId) {
		TextView headerTextView = (TextView)findViewById(textViewResourceId);
	    headerTextView.setText(getString(stringResourceId));	
	}	
    
    protected void setHeaderTextVisibility(boolean visible) {
    	TextView headerTextView = (TextView)findViewById(R.id.headerTextView);
	    headerTextView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	    	case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, SelectChildActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            break;
	    	case R.id.itemInfo:
	    		startActivity(new Intent(this, MyInfoActivity.class));
	    		break;
	    	case R.id.itemPrefs:
	    		startActivity(new Intent(this, PreferencesActivity.class));
	    		break;	    	
	    	case R.id.itemAbout:
	    		AboutDialog.show(this);
	    		break;
	    	case R.id.itemQuit:
	    		PhaetonApplication.getInstance().confirmQuit(this);
	    		break;
    	}
    	
    	return true;
    }	         
    
    protected boolean isNetworkConnectionAvailable() {
    	ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	return (networkInfo != null && networkInfo.isConnected());
    }  
    
    protected void showNetworkConnectionUnavailableToast() {
    	ViewUtil.showToast(this, "Data connection unavailable");
    }             
    
    protected void applyButtonImageWithTextOffset(Button button, int buttonWidth, int image) {
		int textWidth = (int)button.getPaint().measureText(button.getText().toString());
        int padding = (buttonWidth / 2) - ((textWidth / 2) + ((BitmapDrawable)this.getResources().getDrawable(image)).getBitmap().getWidth());
        button.setPadding(padding, button.getPaddingTop(), 0, button.getPaddingBottom());
        button.setCompoundDrawablePadding(-padding);
    }                    
}
