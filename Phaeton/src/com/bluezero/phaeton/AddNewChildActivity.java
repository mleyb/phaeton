package com.bluezero.phaeton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.services.air.HttpAuthenticationException;
import com.bluezero.phaeton.tasks.*;
import com.bluezero.phaeton.utils.*;
import com.bluezero.phaeton.widgets.DatePickerButton;
import com.bugsense.trace.BugSenseHandler;

public class AddNewChildActivity extends PhaetonActivity implements IAsyncTaskResultHandler {
	private static final String TAG = AddNewChildActivity.class.getSimpleName();

	public final static int[] STEPS = new int[] { R.layout.add_new_child_activity1, R.layout.add_new_child_activity2 };

    private ViewFlipper _flipper;
    private Button _nextButton;
        
    private String _parentContactUri;
    private String _imageFilePath;
    
    private CharSequence[] _parentRegistrationNotificationChoices;
	private boolean[] _parentRegistrationNotificationSelections;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_child_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.add_new_child_header);              
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        _flipper = (ViewFlipper)this.findViewById(R.id.flipper);
        
        // inflate the layouts for each step
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int layout : STEPS) {
            View step = inflater.inflate(layout, _flipper, false);
            _flipper.addView(step);
        }
        
        _nextButton = (Button)this.findViewById(R.id.nextButton);
        _nextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (isLastDisplayed()) {
                	confirm();
                } else {
                	// show next step and update buttons
                	_flipper.showNext();
                    updateButtons();
                }
            }
        });
        
        updateButtons();
        
        InputFilter[] nameFilters = new InputFilter[] { new NameInputFilter() };
        
        ((EditText)findViewById(R.id.forenameEditText)).setFilters(nameFilters);
        ((EditText)findViewById(R.id.surnameEditText)).setFilters(nameFilters);
        
        InputFilter[] freeTextFilters = new InputFilter[] { new FreeTextInputFilter() };
        
        ((EditText)findViewById(R.id.notesEditText)).setFilters(freeTextFilters);
        
        if (!DeviceUtil.hasRearCamera()) {
        	((CheckBox)findViewById(R.id.takePictureCheckBox)).setVisibility(View.INVISIBLE);
        }
        
        // configure registration message choices based on device capability
        initialiseParentRegistrationNotificationChoices();        
    }
    
    private void initialiseParentRegistrationNotificationChoices() {
    	ArrayList<String> choices = new ArrayList<String>();    	
		
		if (DeviceUtil.hasTelephony(this)) {
			choices.add("Text");
			choices.add("Email");
			
			_parentRegistrationNotificationSelections = new boolean[2];
			_parentRegistrationNotificationSelections[0] = true;
			_parentRegistrationNotificationSelections[1] = false;
		}
		else {
			choices.add("Email");
			
			_parentRegistrationNotificationSelections = new boolean[1];
			_parentRegistrationNotificationSelections[0] = true;
		}
		
		_parentRegistrationNotificationChoices = choices.toArray(new CharSequence[choices.size()]);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	if (isFirstDisplayed()) {
        		finish();
        	}
        	else {
        		_flipper.showPrevious();
                updateButtons();
        	}
        	
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    protected boolean isFirstDisplayed() {
        return (_flipper.getDisplayedChild() == 0);
    }
    
    protected boolean isLastDisplayed() {
        return (_flipper.getDisplayedChild() == _flipper.getChildCount() - 1);
    }
    
    protected void updateButtons() {
        boolean end = isLastDisplayed();
        
        _nextButton.setText(end ? "Confirm" : "Next");
        
        if (end) {            	
        	Drawable img = getResources().getDrawable(R.drawable.tick_button);
        	_nextButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
	    	super.applyButtonImageWithTextOffset(_nextButton, _nextButton.getWidth(), R.drawable.tick_button);
	    }
        else {
        	_nextButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        	_nextButton.setPadding(0, _nextButton.getPaddingTop(), 0, _nextButton.getPaddingBottom());
        }
    }   
    
    public void linkParentContactClick(View view) {    	
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, Constants.RequestCodes.SELECT_PARENT_CONTACT);        
    }       
        
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == Activity.RESULT_OK) {
	    	switch (requestCode) {
	    		case Constants.RequestCodes.TAKE_PHOTO: {	    			
	    			// create the new child entity
	    			saveData();
	    			
	    			// invoke the media scanner	    			
	    			MediaUtil.mediaScanFile(this, _imageFilePath);
	    			
	    			break;
	    		}
	    		case Constants.RequestCodes.SELECT_IMAGE : {
	    			_imageFilePath = MediaUtil.getImageFilePath(this, data.getData());
	    			
	    			// create the new child entity
	    			saveData();
	    			
	    			break;
	    		}
	    		case Constants.RequestCodes.SELECT_PARENT_CONTACT: {    		    	
					_parentContactUri = data.getData().toString();		 
					
					String contactName = ContactsUtil.getContactName(this, _parentContactUri);		
					
					((Button)findViewById(R.id.linkParentContactButton)).setText("Parent: " + contactName);
					
					break;
	    		}    		
	    	}
    	}
    }       
    
    private void confirm() {
		if (super.isNetworkConnectionAvailable()) {
			EditText forenameEditText = (EditText)findViewById(R.id.forenameEditText);
			String forename = forenameEditText.getText().toString();
			
			if (forename.length() == 0) {				              
				_flipper.showPrevious();  
				updateButtons();
				ViewUtil.setEditTextError(forenameEditText, "You must enter a forename.");
				return;
			}
			
			EditText surnameEditText = (EditText)findViewById(R.id.surnameEditText);
			String surname = surnameEditText.getText().toString();
			
			if (surname.length() == 0) {				
				_flipper.showPrevious();  
				updateButtons();
				ViewUtil.setEditTextError(surnameEditText, "You must enter a surname.");
				return;
			}
			
			if (_parentContactUri == null) {
				ViewUtil.showToast(this, "You must select a parent contact.");
				return;
			}
			
			if (((CheckBox)findViewById(R.id.takePictureCheckBox)).isChecked()) {		        
	        	selectImage();
	        }	
			else {
				saveData();
			}
		}
		else {
			super.showNetworkConnectionUnavailableToast();
		}
    }    
    
    private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Gallery" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Attach Picture");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
					case 0: {
						String filename = UUID.randomUUID().toString(); 
						File imageFile = MediaUtil.createExternalStoragePrivatePicture(AddNewChildActivity.this, filename + ".jpg");
						_imageFilePath = imageFile.getAbsolutePath();
						
				        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
				        
				        // start the image capture Intent
				        startActivityForResult(intent, Constants.RequestCodes.TAKE_PHOTO);

				        break;
					}						
					case 1: {
						Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						intent.setType("image/*");
						startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.RequestCodes.SELECT_IMAGE);
						break;
					}					
				}
			}
		});
		
		builder.show();
	}

    private void saveData() {    	
    	if (super.isNetworkConnectionAvailable()) {	
    		String forename = ((EditText)findViewById(R.id.forenameEditText)).getText().toString();
    		String surname = ((EditText)findViewById(R.id.surnameEditText)).getText().toString();
    		String dateOfBirthUtcString = ((DatePickerButton)findViewById(R.id.dateOfBirthButton)).getUtcFormattedDateString();
    		String notes = ((EditText)findViewById(R.id.notesEditText)).getText().toString();
    		String parentContactUri = (_parentContactUri != null ? _parentContactUri : "");		
    		
    		Child newChild = new Child(forename, surname, dateOfBirthUtcString, notes, parentContactUri);	    		    	    

    		new CreateChildTask(this, this, true).execute(newChild);
		}
		else {
			super.showNetworkConnectionUnavailableToast();
		} 
    }
        
	public <TResult> void handleResult(AsyncTaskResult<TResult> result) {		 
		if (result.getError() != null) {
			if (result.getError() instanceof HttpAuthenticationException) {	
				Log.e(TAG, "Authentication failed!");
				BugSenseHandler.sendException(result.getError());
			}
			
			ViewUtil.showErrorDialog(this, Constants.Errors.GENERAL);

			setResult(Activity.RESULT_CANCELED);				
			finish();
		}
		else {	        		     
			Child child = (Child)result.getResult();
			
			// get the image data, if a photo was taken
			if (_imageFilePath != null) {
				Bitmap bitmap = BitmapFactory.decodeFile(_imageFilePath);
				Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
				thumbnail.compress(CompressFormat.JPEG, 100, stream); 
				byte[] thumbnailData = stream.toByteArray();
			
				child.ImageData = thumbnailData;
			}
			
			// insert to db
	    	new InsertChildToDBTask(this).execute(child);    

	        ViewUtil.showToast(this, "Saved OK");
	        
	        if (((CheckBox)findViewById(R.id.createBirthdayReminderCheckBox)).isChecked()) {
	        	Calendar dateOfBirth = ((DatePickerButton)findViewById(R.id.dateOfBirthButton)).getDateTime();
	        	CalendarUtil.addBirthdayEvent(this, dateOfBirth, 5, (child.Forename + child.Surname));
	        }	        	       	        	              	       
	        
	        String registrationUriString = UriFormatter.formatNewChildRegistrationUri(Constants.Air.NEW_CHILD_REGISTRATION_URI, child.RegistrationCode);
			
	        sendRegistrationMessage(this, child, registrationUriString);
		}		
	}                  	
	
	private void sendRegistrationMessage(final Context context, final Child child, final String registrationUriString) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Phaeton);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		builder.setTitle("Send Parent Registration Message");
		builder.setCancelable(false);
		builder.setMultiChoiceItems(_parentRegistrationNotificationChoices, _parentRegistrationNotificationSelections, new DialogInterface.OnMultiChoiceClickListener() {
		    public void onClick(DialogInterface dialogInterface, int choice, boolean selected) {
		        _parentRegistrationNotificationSelections[choice] = selected;
		    }		
		});		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				
				if (AddNewChildActivity.this._parentRegistrationNotificationSelections[0]) {
					SmsUtil.launchNewChildRegistrationSmsIntent(context, child, registrationUriString);																											
				}
				
				if (AddNewChildActivity.this._parentRegistrationNotificationSelections[1]) {
					EmailUtil.launchNewChildRegistrationEmailIntent(context, child, registrationUriString);																											
				}
								
				setResult(Activity.RESULT_OK);	        
		        finish();	
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
}