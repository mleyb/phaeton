package com.bluezero.phaeton;

import java.util.ArrayList;

import roboguice.inject.InjectView;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.vending.billing.IabHelper;
import com.android.vending.billing.IabResult;
import com.android.vending.billing.Inventory;
import com.bluezero.phaeton.models.Carer;
import com.bluezero.phaeton.tasks.InitialiseSessionTask;
import com.bluezero.phaeton.tasks.RefreshAccessTokenTask;
import com.bluezero.phaeton.utils.AsyncTaskResult;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.IntentUtil;
import com.bluezero.phaeton.utils.ViewUtil;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;

public class SignInActivity extends PhaetonActivity {
	private static final String TAG = SignInActivity.class.getSimpleName();
		
	@InjectView(R.id.signInWithGoogleButton) Button _signInWithGoogleButton;
	@InjectView(R.id.signInProgressBar) ProgressBar _progressBar;
	@InjectView(R.id.messageTextView) TextView _messageTextView;
	
	private IabHelper _iabHelper;
    private LicenseChecker _licenseChecker;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	    	   	   	    	   	   	   
	    
        String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);        

        AESObfuscator obfuscator = new AESObfuscator(License.getSalt(), getPackageName(), deviceId);
        _licenseChecker = new LicenseChecker(this, new ServerManagedPolicy(this, obfuscator), License.getPublicKey());               
	    
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
        
	    boolean autoSignIn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("signInAutomatically", false);
	    
	    if (autoSignIn && SessionInfo.CarerId != null) {
	    	if (SessionInfo.Token != null) {
	    		launchSelectChildActivity();
	    	}
	    }
	    
    	setContentView(R.layout.signin_activity);
    	
    	setHeaderTextVisibility(false); 
    	
    	setMessageText(Html.fromHtml(Constants.Messages.PLEASE_SIGN_IN), false);	   
    	
    	ProgressBar progressBar = (ProgressBar)findViewById(R.id.signInProgressBar);
    	progressBar.setIndeterminateDrawable(getResources().getDrawable(com.bluezero.phaeton.R.drawable.progress));
	}	
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (_iabHelper != null) {
        	_iabHelper.dispose();
        	_iabHelper = null;
        }
        
        _licenseChecker.onDestroy();
        _licenseChecker = null;        
    }   
    
	public void signInWithGoogleClick(View view) {			
        if (super.isNetworkConnectionAvailable()) {
        	beginSignIn();
		}
        else { 
			super.showNetworkConnectionUnavailableToast();				
        }
	}			
	
	private void setMessageText(Spanned message, boolean error) {
		if (error) {		
			_messageTextView.setTypeface(null, Typeface.BOLD);
			_messageTextView.setTextColor(getResources().getColor(R.color.text_error));
			_messageTextView.setText(message);
		}
		else {
			_messageTextView.setTypeface(null, Typeface.NORMAL);
			_messageTextView.setTextColor(getResources().getColor(R.color.text_regular));
			_messageTextView.setText(message);
		}		
	}
		
	private void launchSelectChildActivity() {
		Intent intent = new Intent(getApplicationContext(), SelectChildActivity.class);
        startActivity(intent);
        finish();
	}
		
	// 1
	private void beginSignIn() {
		runOnUiThread(new Runnable() {
            public void run() {
            	setMessageText(Html.fromHtml("Signing in..."), false);
            }
        }); 
		
		// first we select the account to use...
		Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false, null, null, null, null);		
		startActivityForResult(intent, Constants.RequestCodes.CHOOSE_ACCOUNT);
	}
	
	// 2
	private void checkLicense() {
		runOnUiThread(new Runnable() {
            public void run() {
        		setMessageText(Html.fromHtml("Checking licence..."), false);
            }
		});
		
		// then check the license validity
		_licenseChecker.checkAccess(new LicenseCheckCompletedCallback());
	}
	
	// 3
	private void refreshAccessToken() {
		runOnUiThread(new Runnable() {
            public void run() {        
            	setMessageText(Html.fromHtml("Signing in with Google..."), false);
            }
		});
		
        new RefreshAccessTokenTask(this, new RefreshAccessTokenResultHandler(this)).execute();
	}
	
	// 4
	private void initialiseSession() {
		runOnUiThread(new Runnable() {
            public void run() {
            	setMessageText(Html.fromHtml("Retrieving your details..."), false);
            }
		});
				
		new InitialiseSessionTask(this, new InitialiseSessionResultHandler(this)).execute();
	}
	
	// 5
	private void queryIab() {
		runOnUiThread(new Runnable() {
            public void run() {
            	setMessageText(Html.fromHtml("Checking <font color='green'><b>Pro</b></font> status..."), false);
            }
		});
		
		ArrayList<String> additionalSkuList = new ArrayList<String>();
		additionalSkuList.add(Constants.SKU.UPGRADE_TO_PRO);
		_iabHelper.queryInventoryAsync(true, additionalSkuList, new IabQueryInventoryFinishedListener(this));
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.RequestCodes.CHOOSE_ACCOUNT && resultCode == RESULT_OK) {
			_signInWithGoogleButton.setEnabled(false);
			_progressBar.setVisibility(View.VISIBLE);    				  
    	
			SessionInfo.AccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
    	
			// we have the account, so now check the license, on completion begin the rest of sign-in
			checkLicense();
		}
		else if (requestCode == Constants.RequestCodes.LAUNCH_AUTH_INTENT) {
			if (resultCode == RESULT_OK) {
				// attempt to continue with sign-in
				refreshAccessToken();
			}
			else {
				_signInWithGoogleButton.setEnabled(true);
				_progressBar.setVisibility(View.INVISIBLE);      
				setMessageText(Html.fromHtml(Constants.Messages.PLEASE_SIGN_IN), false);	 
			}
		}
	}
				
	// License Handling
	
	private void displayLicenseDialog(final boolean showRetry) {
        runOnUiThread(new Runnable() {
            public void run() {
                showDialog(showRetry ? 1 : 0);
            }
        });
    } 
	
    protected Dialog onCreateDialog(int id) {
        final boolean retry = (id == 1);
        return new AlertDialog.Builder(this)
            .setTitle(R.string.unlicensed_dialog_title)
            .setMessage(retry ? R.string.unlicensed_dialog_retry_body : R.string.unlicensed_dialog_body)
            .setPositiveButton(retry ? R.string.retry_button : R.string.buy_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (retry) {
                    	_licenseChecker.checkAccess(new LicenseCheckCompletedCallback());
                    } 
                    else {
                        Intent marketIntent = IntentUtil.generateMarketIntent(getPackageName());
                        startActivity(marketIntent);                        
                    }
                }
            })
            .setNegativeButton(R.string.quit_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).create();
    }
	
	private class LicenseCheckCompletedCallback implements LicenseCheckerCallback {
        public void allow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
                    	
            Log.i(TAG, "License check OK. Continuing sign-in with attempt to get access token...");            
        	refreshAccessToken();
        }

        public void dontAllow(int policyReason) {
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }

            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to Market.
            // If the reason for the lack of license is that the service is
            // unavailable or there is another problem, we display a
            // retry button on the dialog and a different message.
            displayLicenseDialog(policyReason == Policy.RETRY);
        }

        public void applicationError(int errorCode) {        	
            String result = String.format("Application error: %1$s", errorCode);
            Log.e(TAG, result);
            
            runOnUiThread(new Runnable() {
                public void run() {
                	_signInWithGoogleButton.setEnabled(true);
        			_progressBar.setVisibility(View.INVISIBLE);
                	setMessageText(Html.fromHtml("An error occurred. Please try again."), true);
                }
            });                      
        }
    }	
	
	// Refresh Access Token Handler
	
	private class RefreshAccessTokenResultHandler implements IAsyncTaskResultHandler {
		private final SignInActivity _parentActivity;
    	
    	public RefreshAccessTokenResultHandler(SignInActivity parentActivity) {
    		_parentActivity = parentActivity;
    	}
		
		public <T> void handleResult(AsyncTaskResult<T> result) {
			if (result.getError() == null) {
				initialiseSession();				
			}
			else {
				if (result.getError() instanceof GooglePlayServicesAvailabilityException) {	
					int statusCode = ((GooglePlayServicesAvailabilityException)result.getError()).getConnectionStatusCode();
					Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, _parentActivity, Constants.RequestCodes.LAUNCH_AUTH_INTENT);
					dialog.show();
				}
				else if (result.getError() instanceof UserRecoverableAuthException) {
					Intent recoveryIntent = ((UserRecoverableAuthException)result.getError()).getIntent();
					_parentActivity.startActivityForResult(recoveryIntent, Constants.RequestCodes.LAUNCH_AUTH_INTENT);
				}
				else {
					// couldn't get a token for some reason
					_signInWithGoogleButton.setEnabled(true);
					_progressBar.setVisibility(View.INVISIBLE);      
					setMessageText(Html.fromHtml("Sign-in failed. Please try again."), true);	
				}
			}
		}    	
    }
	
	// Initialise Session Handler
	
	private class InitialiseSessionResultHandler implements IAsyncTaskResultHandler {
    	private final SignInActivity _parentActivity;
    	
    	public InitialiseSessionResultHandler(SignInActivity parentActivity) {
    		_parentActivity = parentActivity;
    	}
    	
		public <T> void handleResult(AsyncTaskResult<T> result) {
			if (result.getError() == null) {			
				Carer carer = (Carer)result.getResult();
				
				SessionInfo.CarerId = carer.Id;
				SessionInfo.Key = carer.Key;
				SessionInfo.DisplayName = carer.Name;
				SessionInfo.save(_parentActivity);						
				
				queryIab();
			}
	        else {
	        	_signInWithGoogleButton.setEnabled(true);
	        	_progressBar.setVisibility(View.INVISIBLE);    	
	        	setMessageText(Html.fromHtml(result.getError().getMessage()), true);
	        }
		} 		
    }	
	
	// IAB Query Finished Listener
	
	private class IabQueryInventoryFinishedListener implements IabHelper.QueryInventoryFinishedListener {
		private final SignInActivity _parentActivity;
    	
    	public IabQueryInventoryFinishedListener(SignInActivity parentActivity) {
    		_parentActivity = parentActivity;
    	}		
		
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			if (!result.isFailure()) {				
				boolean isPro = inventory.hasPurchase(Constants.SKU.UPGRADE_TO_PRO);       							

				SessionInfo.IsProfessional = isPro;
				SessionInfo.save(_parentActivity);							
		        
	        	ViewUtil.showToast(_parentActivity, "Hi " + SessionInfo.DisplayName + "!");
	        	
	        	runOnUiThread(new Runnable() {
	                public void run() {
	    	        	_progressBar.setVisibility(View.INVISIBLE);
	                }
	            });
	        		        	
	        	// sign-in sequence completed
		        launchSelectChildActivity();	 
			}
			else {
				runOnUiThread(new Runnable() {
	                public void run() {
	                	_signInWithGoogleButton.setEnabled(true);
	                	_progressBar.setVisibility(View.INVISIBLE);      
	                	setMessageText(Html.fromHtml("Sign-in failed. Please try again."), true);
	                }
				});
			}
		}
	}
}
