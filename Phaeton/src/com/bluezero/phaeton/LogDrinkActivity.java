package com.bluezero.phaeton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.bluezero.phaeton.models.Drink;
import com.bluezero.phaeton.tasks.LogDrinkTask;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;
import com.bluezero.phaeton.widgets.TimePickerButton;

public class LogDrinkActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_drink_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.drink_header);

        getActionBar().setDisplayHomeAsUpEnabled(true);
	}
    
    public void saveData() {
    	EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);			
		String description = descriptionEditText.getText().toString().trim();
		 				
		if (description.length() == 0) {
			ViewUtil.setEditTextError(descriptionEditText, "You must enter a description.");					
			return;
		}
    	
		RadioGroup unitRadioGroup = (RadioGroup)findViewById(R.id.unitRadioGroup);	
    	
		String date = ((TimePickerButton)findViewById(R.id.timeButton)).getUtcFormattedDateString();		
		double amount = Double.parseDouble(((EditText)findViewById(R.id.amountEditText)).getText().toString());				
		int unit = unitRadioGroup.indexOfChild(findViewById(unitRadioGroup.getCheckedRadioButtonId()));
		
		new LogDrinkTask(this, this, getChild().Id).execute(new Drink(date, description, amount, unit));
    }	
}
