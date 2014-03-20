package com.bluezero.phaeton.widgets;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.bluezero.phaeton.utils.DateTimeUtil;

public abstract class DateTimePickerButton extends Button {
	protected Calendar _calendar = Calendar.getInstance();

	public Calendar getDateTime() {
		return _calendar;
	}
	
	public DateTimePickerButton(Context context) {
		super(context);
		setOnClickListener(_onClickListener);
		updateButtonText();
	}

	public DateTimePickerButton(Context context, AttributeSet attrs){
		super(context, attrs);
		setOnClickListener(_onClickListener);
		updateButtonText();
	}	
	
	public String getUtcFormattedDateString() {		
		return DateTimeUtil.toUTCFormattedString(_calendar.getTime());
	}
		
	private OnClickListener _onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            showPickerDialog();        	
        }
    };
    
    protected abstract void showPickerDialog();
    
    protected abstract void updateButtonText();
}
