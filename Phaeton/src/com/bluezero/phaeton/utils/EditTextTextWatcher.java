package com.bluezero.phaeton.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditTextTextWatcher implements TextWatcher
{
	private EditText _target;
	
	public EditTextTextWatcher(EditText target) {
		_target = target;
	}
	
	public void afterTextChanged(Editable s) {
		if (s.length() > 0) {
			_target.setError(null);
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub		
	}
};