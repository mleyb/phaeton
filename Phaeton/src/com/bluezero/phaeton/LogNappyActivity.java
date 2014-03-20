package com.bluezero.phaeton;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bluezero.phaeton.models.Nappy;
import com.bluezero.phaeton.tasks.LogNappyTask;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.widgets.TimePickerButton;

public class LogNappyActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {
	
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_nappy_activity);               
        
        super.setHeaderText(R.id.headerTextView, R.string.nappy_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	 public void saveData() {
		boolean dirty = ((CheckBox)findViewById(R.id.dirtyCheckBox)).isChecked();	    		    		    	    	
		String date = ((TimePickerButton)findViewById(R.id.timeButton)).getUtcFormattedDateString();	    		    	    	
		String detail = ((EditText)findViewById(R.id.detailsEditText)).getText().toString().trim();
		
		new LogNappyTask(this, this, getChild().Id).execute(new Nappy(date, dirty, detail));
	}
}
