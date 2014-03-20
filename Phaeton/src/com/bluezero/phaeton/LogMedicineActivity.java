package com.bluezero.phaeton;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;

import com.bluezero.phaeton.models.Medicine;
import com.bluezero.phaeton.tasks.LogMedicineTask;
import com.bluezero.phaeton.utils.FreeTextInputFilter;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;
import com.bluezero.phaeton.widgets.TimePickerButton;

public class LogMedicineActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_medicine_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.medicine_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        InputFilter[] filters = new InputFilter[] { new FreeTextInputFilter() };
        
        ((EditText)findViewById(R.id.typeEditText)).setFilters(filters);
        ((EditText)findViewById(R.id.amountEditText)).setFilters(filters);
        ((EditText)findViewById(R.id.detailsEditText)).setFilters(filters);
    }
	
	public void saveData() {
		EditText typeEditText = (EditText)findViewById(R.id.typeEditText);			
		String type = typeEditText.getText().toString().trim();
		 				
		if (type.length() == 0) {
			ViewUtil.setEditTextError(typeEditText, "You must enter a medicine type.");
			return;
		}
		
		EditText amountEditText = (EditText)findViewById(R.id.amountEditText);			
		String amount = amountEditText.getText().toString().trim();
		 				
		if (amount.length() == 0) {
			ViewUtil.setEditTextError(amountEditText, "You must enter an amount.");
			return;
		}
		
		String date = ((TimePickerButton)findViewById(R.id.timeButton)).getUtcFormattedDateString();
		String detail = ((EditText)findViewById(R.id.detailsEditText)).getText().toString().trim();
				
		new LogMedicineTask(this, this, getChild().Id).execute(new Medicine(date, amount, type, detail));
    }
}
