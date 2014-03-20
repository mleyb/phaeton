package com.bluezero.phaeton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bluezero.phaeton.models.Activity;
import com.bluezero.phaeton.tasks.LogActivityTask;
import com.bluezero.phaeton.utils.DateTimeUtil;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;

public class LogActivityActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_activity_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.activity_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
   	}
	
	public void confirmClick(View view) {
		saveData();
    }
	
	@Override
	protected void saveData() {
		EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);			
		String description = descriptionEditText.getText().toString().trim();
		 				
		if (description.length() == 0) {
			ViewUtil.setEditTextError(descriptionEditText, "You must enter a description.");
			return;
		}

		String date = DateTimeUtil.getCurrentDateJSONString();
		String detail = ((EditText)findViewById(R.id.detailsEditText)).getText().toString().trim();
		
		new LogActivityTask(this, this, getChild().Id).execute(new Activity(date, description, detail));
	}
}
