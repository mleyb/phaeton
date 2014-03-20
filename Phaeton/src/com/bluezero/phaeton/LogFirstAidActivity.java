package com.bluezero.phaeton;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bluezero.phaeton.models.FirstAid;
import com.bluezero.phaeton.tasks.LogFirstAidTask;
import com.bluezero.phaeton.utils.DateTimeUtil;
import com.bluezero.phaeton.utils.FreeTextInputFilter;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;

public class LogFirstAidActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_first_aid_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.first_aid_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        InputFilter[] filters = new InputFilter[] { new FreeTextInputFilter() };
        
        ((EditText)findViewById(R.id.reasonEditText)).setFilters(filters);
        ((EditText)findViewById(R.id.descriptionEditText)).setFilters(filters);
        ((EditText)findViewById(R.id.detailsEditText)).setFilters(filters);
	}
	
	public void saveData() {
		EditText reasonEditText = (EditText)findViewById(R.id.reasonEditText);			
		String reason = reasonEditText.getText().toString().trim();
		 				
		if (reason.length() == 0) {
			reasonEditText.setError("You must enter a reason.");
			reasonEditText.setFocusableInTouchMode(true);
			reasonEditText.requestFocus();					
			return;
		}
		
		EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);			
		String description = descriptionEditText.getText().toString().trim();
		 				
		if (description.length() == 0) {
			ViewUtil.setEditTextError(descriptionEditText, "You must enter a description.");					
			return;
		}
		
		String date = DateTimeUtil.getCurrentDateJSONString();
		String detail = ((EditText)findViewById(R.id.detailsEditText)).getText().toString().trim();     		
		boolean doctorRecommended = ((CheckBox)findViewById(R.id.doctorRecommendedCheckBox)).isChecked();
				
		new LogFirstAidTask(this, this, getChild().Id).execute(new FirstAid(date, reason, description, detail, doctorRecommended));
    } 
}
