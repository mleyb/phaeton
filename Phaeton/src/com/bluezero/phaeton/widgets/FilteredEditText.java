package com.bluezero.phaeton.widgets;

import com.bluezero.phaeton.utils.EditTextTextWatcher;
import com.bluezero.phaeton.utils.FreeTextInputFilter;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

public class FilteredEditText extends EditText {

	public FilteredEditText(Context context) {
		super(context);
		initialise();
	}

	public FilteredEditText(Context context, AttributeSet attrs){
		super(context, attrs);
		initialise();
	}
	
	private void initialise() {
		InputFilter[] filters = new InputFilter[] { new FreeTextInputFilter() };   
        setFilters(filters);
        
        addTextChangedListener(new EditTextTextWatcher(this));        
	}
}
