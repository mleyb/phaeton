package com.bluezero.phaeton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.bluezero.phaeton.models.Bottle;
import com.bluezero.phaeton.tasks.LogBottleTask;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.widgets.TimePickerButton;

public class LogBottleActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_bottle_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.bottle_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }             
	
    protected void saveData() {
    	RadioGroup unitRadioGroup = (RadioGroup)findViewById(R.id.unitRadioGroup);	
    	
		String date = ((TimePickerButton)findViewById(R.id.timeButton)).getUtcFormattedDateString();		
		double amount = Double.parseDouble(((EditText)findViewById(R.id.amountEditText)).getText().toString());				
		int unit = unitRadioGroup.indexOfChild(findViewById(unitRadioGroup.getCheckedRadioButtonId()));
		
		new LogBottleTask(this, this, getChild().Id).execute(new Bottle(date, amount, unit));
    }
}
