package com.bluezero.phaeton.widgets;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class TimePickerButton extends DateTimePickerButton {	
	public TimePickerButton(Context context) {
		super(context);
	}

	public TimePickerButton(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	private TimePickerDialog.OnTimeSetListener _timeSetListener = new TimePickerDialog.OnTimeSetListener() { 
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			_calendar.set(Calendar.MINUTE, minute);	
			updateButtonText();
		}
    };
    
	@Override
	protected void showPickerDialog() {
		new TimePickerDialog(getContext(), com.bluezero.phaeton.R.style.DialogPhaeton, _timeSetListener, _calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE), false).show();
	}

	@Override
	protected void updateButtonText() {	
		StringBuilder builder = new StringBuilder();		
		builder.append(String.format("%02d", _calendar.get(Calendar.HOUR)));
		builder.append(":");
		builder.append(String.format("%02d", _calendar.get(Calendar.MINUTE)));
		builder.append(" ");
		builder.append(_calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");			
    	setText(builder);		
	}
}