package com.bluezero.phaeton;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bluezero.phaeton.models.Sick;
import com.bluezero.phaeton.tasks.LogSickTask;
import com.bluezero.phaeton.utils.FreeTextInputFilter;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;
import com.bluezero.phaeton.widgets.TimePickerButton;

public class LogSickActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_sick_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.sick_header);
        
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
		
		String date = ((TimePickerButton)findViewById(R.id.timeButton)).getUtcFormattedDateString();
		String detail = ((EditText)findViewById(R.id.detailsEditText)).getText().toString().trim();     		
		boolean doctorRecommended = ((CheckBox)findViewById(R.id.doctorRecommendedCheckBox)).isChecked();
						
		new LogSickTask(this, this, getChild().Id).execute(new Sick(date, description, detail, doctorRecommended));
    }
}
