package com.bluezero.phaeton;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.vending.billing.IabHelper;
import com.android.vending.billing.IabResult;
import com.android.vending.billing.Purchase;
import com.bluezero.phaeton.data.DataSource;
import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.services.air.HttpAuthenticationException;
import com.bluezero.phaeton.tasks.DeleteChildTask;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.ContactsUtil;
import com.bluezero.phaeton.utils.DeviceUtil;
import com.bluezero.phaeton.utils.EmailUtil;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.IntentUtil;
import com.bluezero.phaeton.utils.SmsUtil;
import com.bluezero.phaeton.utils.ViewUtil;
import com.bugsense.trace.BugSenseHandler;

// Main Activity
public class SelectChildActivity extends PhaetonActivity implements IAsyncTaskResultHandler {
	private static final String TAG = SelectChildActivity.class.getSimpleName();

	private static final int MaxCountBeforePro = 2;
	
	@InjectView(R.id.headerTextView) TextView _headerTextView;
	@InjectView(R.id.childListView) ListView _listView;
	
	private ChildSelectionArrayAdapter _listAdapter;
	
	private IabHelper _iabHelper;
	
	// GCM hooks
	private GCMConnector _gcm;	
	private GCMBroadcastReceiver _gcmReceiver;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {		
        super.onCreate(savedInstanceState);
        
        try { 
        	StrictModeWrapper.init(this); 
        } 
        catch (Throwable throwable) { 
        	Log.v("StrictMode", "StrictMode is not available."); 
        }                 	                
        
	    setContentView(R.layout.select_child_activity);              
	    
	    _headerTextView.setVisibility(View.GONE);
	              
	    // initialise and register gcm receiver
	    _gcmReceiver = new GCMBroadcastReceiver(this);	    	   	    
	    _gcmReceiver.register(); 
	    	    
	    // register with GCM
	    _gcm = new GCMConnector();
	    _gcm.register(this);	    
	     
	    if (!SessionInfo.IsProfessional) {
	    	_iabHelper = new IabHelper(this, License.getPublicKey());
	        
	        _iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
	        	@Override
	        	public void onIabSetupFinished(IabResult result) {
	        		if (!result.isSuccess()) {
	        	         Log.e(TAG, "Problem setting up In-app Billing: " + result);
	        	    }
	        		else {
	        			Log.i(TAG, "In-app billing set up successfully");
	        		}	        		
	        	}
	        });
	    }
	    
        populateListView();        
               		
        registerForContextMenu(_listView);
        
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				onListItemClick(adapterView, position);
			}
		});
		
		WhatsNew.showWhatsNewDialog(this);
	}
    		
	@Override
    protected void onDestroy() {		
		_gcmReceiver.unregister();
        _gcm.unregister(this);
		
        if (_iabHelper != null) {
        	_iabHelper.dispose();
        	_iabHelper = null;
        }
        
        super.onDestroy();
	}   
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if (SessionInfo.IsProfessional) {
    		getMenuInflater().inflate(R.menu.pro_select_child_menu, menu);        	
    	}
    	else {
    		getMenuInflater().inflate(R.menu.select_child_menu, menu);
    	}
    	return true;
    }
	 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	    
    	if (item.getItemId() == R.id.itemNew) {
    		if (_listView.getCount() >= MaxCountBeforePro && !SessionInfo.IsProfessional) {
    			_iabHelper.launchPurchaseFlow(this, Constants.SKU.UPGRADE_TO_PRO, Constants.RequestCodes.PURCHASE_PRO_UPGRADE, new OnIabPurchaseFinishedListener(this), SessionInfo.Key);
    		}
    		else {
    			startActivityForResult(new Intent(this, AddNewChildActivity.class), Constants.RequestCodes.CREATE_NEW_CHILD);
    		}
    	}
    	else {
    		if (item.getItemId() == R.id.itemUpgrade) {
    			_iabHelper.launchPurchaseFlow(this, Constants.SKU.UPGRADE_TO_PRO, Constants.RequestCodes.PURCHASE_PRO_UPGRADE, new OnIabPurchaseFinishedListener(this), SessionInfo.Key);    			
    		}
    		else {    		
    			super.onOptionsItemSelected(item);
			}    		
    	}
    	
    	return true;
    }	         
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    
	    LinearLayout layout = (LinearLayout)info.targetView;
	    TextView itemTitleTextView = (TextView)layout.findViewById(android.R.id.text1);
	    
	    menu.setHeaderTitle(itemTitleTextView.getText());
	    
	    ArrayList<String> items = new ArrayList<String>();

	    items.add("Show Details");
	    
	    if (DeviceUtil.hasTelephony(this)) {
	    	items.add("Call Parent");
	    	items.add("Message Parent");
	    }
	    
	    items.add("Email Parent");
	    items.add("Open Parent");
	    items.add("Delete");
	    
	    for (int i = 0; i < items.size(); i++) {
	    	menu.add(Menu.NONE, i, i, items.get(i));
	    }    	    
	}  
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		Child selectedChild = _listAdapter.getItem(info.position);
		switch (item.getItemId()) {
			case 0: {
				Intent intent = IntentUtil.generateIntent(getApplicationContext(), ChildInfoActivity.class, selectedChild);		
				startActivity(intent);
				break;
			}
			case 1: {
				String number = "tel:" + ContactsUtil.getContactNumber(this, selectedChild.ParentContactUri).trim();			
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
				startActivity(intent);
				break;				
			}
			case 2: {
				SmsUtil.launchSmsIntent(this, selectedChild.ParentContactUri, (String)null);
				break;
			}			
			case 3: {
				EmailUtil.launchEmailIntent(this, selectedChild.ParentContactUri, (String)null, (String)null);
				break;
			}
			case 4: {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedChild.ParentContactUri));
				startActivity(intent);
				break;
			}
			case 5: {
				confirmDelete(this, selectedChild);
				break;
			}
		}
		
		return true;
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	PhaetonApplication.getInstance().confirmQuit(this);
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }

    public void addNewChildClick(View view) {
    	startActivityForResult(new Intent(getApplicationContext(), AddNewChildActivity.class), Constants.RequestCodes.CREATE_NEW_CHILD);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (_iabHelper.handleActivityResult(requestCode, resultCode, data)) {
        	if (resultCode == RESULT_OK) {
        		ViewUtil.showPurchaseThanksDialog(this, "Thankyou for your purchase!");
			}        	
        }
        else {
        	if (requestCode == Constants.RequestCodes.CREATE_NEW_CHILD) {    			
    			populateListView();
    		}
        }
    }      
    
    private void onListItemClick(AdapterView<?> adapterView, int position) {
		Child child = (Child)adapterView.getItemAtPosition(position);
		Intent intent = IntentUtil.generateIntent(getApplicationContext(), SelectActionActivity.class, child);		
		startActivity(intent);
	}
		
	private void populateListView() {
		List<Child> children = null;		
        DataSource dataSource = new DataSource(getApplicationContext());
        
		try {                	        
        	children = dataSource.getAll(false);
        }
        finally {
        	dataSource.cleanup();
        }              
		
		Child[] rows = children.toArray(new Child[children.size()]);
		
		_listAdapter = new ChildSelectionArrayAdapter(this, rows);
				           	        	
		_listView.setEmptyView(findViewById(android.R.id.empty));
		_listView.setAdapter(_listAdapter);
	}
	
	private void confirmDelete(final Context context, final Child child) {
		ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Phaeton);
		new AlertDialog.Builder(ctw)
			.setTitle(R.string.app_name)
			.setIcon(android.R.drawable.ic_delete)
			.setMessage("Are you sure you want to delete this child?")
			.setCancelable(false)
			.setNegativeButton("No", null)		
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					deleteChild(context, child);
				}
		}).show();
	}	
	
	private void deleteChild(Context context, Child child) {
        if (super.isNetworkConnectionAvailable()) {
        	new DeleteChildTask(this, this).execute(child);		            
		}
        else { 
			super.showNetworkConnectionUnavailableToast();			
        }
	}

	public <T> void handleResult(AsyncTaskResult<T> result) {
		if (result.getError() != null) {
			if (result.getError() instanceof HttpAuthenticationException) {
				Log.e(TAG, "Authentication failed!");
				BugSenseHandler.sendException(result.getError());
			}

			ViewUtil.showErrorDialog(this, Constants.Errors.GENERAL);
		}
		else {	        				
			Child child = (Child)result.getResult();			
			
			DataSource dataSource = new DataSource(this);
			try {
				// mark as deleted and update record
				child.Deleted = true;
				dataSource.update(child);
			}
			finally {
				dataSource.cleanup();
			}

			// refresh the list from the newly updated local db cache
			populateListView();
			
			ViewUtil.showToast(this, "Deleted OK");
		}
	}
	
	public class ChildSelectionArrayAdapter extends ImageTwoLineTextArrayAdapter<Child> {
	    public ChildSelectionArrayAdapter(Context context, Child[] children) {
	        super(context, R.layout.select_child_list_item, children);
	    }

	    @Override
	    public String lineOneText(Child child) {
	        return child.Forename + " " + child.Surname;
	    }

	    @Override
	    public String lineTwoText(Child child) {
	    	return ContactsUtil.getContactName(super.getContext(), child.ParentContactUri);
	    }

		@Override
		public Bitmap lineImage(Child child) {
			Bitmap image = child.getImage();						
			if (image == null) {
				// use default image
				image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
			}
			
			return image;
		}
	}
	
	private class OnIabPurchaseFinishedListener implements IabHelper.OnIabPurchaseFinishedListener {
		private SelectChildActivity _parentActivity;
		
		public OnIabPurchaseFinishedListener(SelectChildActivity parentActivity) {
			_parentActivity = parentActivity;
		}
		
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				Log.d(TAG, "Error purchasing: " + result);
				return;
			}      
			else if (purchase.getSku().equals(Constants.SKU.UPGRADE_TO_PRO) && purchase.getDeveloperPayload().equals(SessionInfo.Key)) {
				SessionInfo.IsProfessional = true;
				SessionInfo.save(_parentActivity);
			}
		}
	};
}	