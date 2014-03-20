package com.bluezero.phaeton;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bluezero.phaeton.models.Drink;
import com.bluezero.phaeton.models.Snack;
import com.bluezero.phaeton.tasks.LogDrinkTask;
import com.bluezero.phaeton.tasks.LogSnackTask;
import com.bluezero.phaeton.utils.FreeTextInputFilter;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;
import com.bluezero.phaeton.widgets.TimePickerButton;

public class LogSnackActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_snack_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.snack_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        InputFilter[] filters = new InputFilter[] { new FreeTextInputFilter() };
        
        ((EditText)findViewById(R.id.descriptionEditText)).setFilters(filters);
        
        String[] amounts = getResources().getStringArray(R.array.snack_amount_consumed_items);       
        
        ArrayAdapter<String> amountAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, amounts);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner amountSpinner = (Spinner)findViewById(R.id.spinnerAmount);
        amountSpinner.setAdapter(amountAdapter);
	}
	
	public void saveData() {
		EditText descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);			
		String description = descriptionEditText.getText().toString().trim();
		 				
		if (description.length() == 0) {
			ViewUtil.setEditTextError(descriptionEditText, "You must enter a description.");					
			return;
		}
		
		String date = ((TimePickerButton)findViewById(R.id.timeButton)).getUtcFormattedDateString();
		int amount = ((Spinner)findViewById(R.id.spinnerAmount)).getSelectedItemPosition();
				
		new LogSnackTask(this, this, getChild().Id).execute(new Snack(date, description, amount));
    }
}
