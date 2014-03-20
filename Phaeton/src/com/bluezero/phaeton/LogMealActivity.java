package com.bluezero.phaeton;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bluezero.phaeton.models.Meal;
import com.bluezero.phaeton.tasks.LogMealTask;
import com.bluezero.phaeton.utils.DateTimeUtil;
import com.bluezero.phaeton.utils.FreeTextInputFilter;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;

public class LogMealActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_meal_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.meal_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        String[] mealTypes = getResources().getStringArray(R.array.meal_type_items);       
        
        ArrayAdapter<String> mealTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mealTypes);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner mealTypeSpinner = (Spinner)findViewById(R.id.spinnerMeal);
        mealTypeSpinner.setAdapter(mealTypeAdapter);      
        
        InputFilter[] filters = new InputFilter[] { new FreeTextInputFilter() };
               
        ((EditText)findViewById(R.id.descriptionEditText)).setFilters(filters);
        
        String[] amounts = getResources().getStringArray(R.array.meal_amount_consumed_items);       
        
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
		
		String date = DateTimeUtil.getCurrentDateJSONString();
		int meal = ((Spinner)findViewById(R.id.spinnerMeal)).getSelectedItemPosition();
		int amount = ((Spinner)findViewById(R.id.spinnerAmount)).getSelectedItemPosition();
				
		new LogMealTask(this, this, getChild().Id).execute(new Meal(date, meal, description, amount));
    }
}
