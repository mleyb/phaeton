package com.bluezero.phaeton.widgets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;

public class DatePickerButton extends DateTimePickerButton {
	public DatePickerButton(Context context) {
		super(context);
	}

	public DatePickerButton(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	private DatePickerDialog.OnDateSetListener _dateSetListener = new DatePickerDialog.OnDateSetListener() { 
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	_calendar.set(Calendar.YEAR, year);                	
        	_calendar.set(Calendar.MONTH, monthOfYear);
        	_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateButtonText();
        }
    };

	@Override
	protected void showPickerDialog() {
		new DatePickerDialog(getContext(), com.bluezero.phaeton.R.style.DialogPhaeton, _dateSetListener, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH), _calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	@Override
    protected void updateButtonText() {
		Date date = _calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.UK); 
		String dayName = dateFormat.format(date);		
    	setText(new StringBuilder().append(dayName).append(" ").append(String.format("%02d", _calendar.get(Calendar.DAY_OF_MONTH))).append("/").append(String.format("%02d", _calendar.get(Calendar.MONTH) + 1)).append("/").append(_calendar.get(Calendar.YEAR)));
    }
}
