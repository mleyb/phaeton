package com.bluezero.phaeton;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;

import com.bluezero.phaeton.models.Milestone;
import com.bluezero.phaeton.tasks.LogMilestoneTask;
import com.bluezero.phaeton.utils.DateTimeUtil;
import com.bluezero.phaeton.utils.FreeTextInputFilter;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;

public class LogMilestoneActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_milestone_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.milestone_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        InputFilter[] filters = new InputFilter[] { new FreeTextInputFilter() };
        
        ((EditText)findViewById(R.id.descriptionEditText)).setFilters(filters);
        ((EditText)findViewById(R.id.detailsEditText)).setFilters(filters);
	}
	
	public void saveData() {
		EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);			
		String description = descriptionEditText.getText().toString().trim();
		 				
		if (description.length() == 0) {
			ViewUtil.setEditTextError(descriptionEditText, "You must enter a description.");					
			return;
		}
		
		String date = DateTimeUtil.getCurrentDateJSONString();
		String detail = ((EditText)findViewById(R.id.detailsEditText)).getText().toString().trim();
				
		new LogMilestoneTask(this, this, getChild().Id).execute(new Milestone(date, description, detail));
    }
}
