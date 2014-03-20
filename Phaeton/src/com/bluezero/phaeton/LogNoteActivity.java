package com.bluezero.phaeton;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;

import com.bluezero.phaeton.models.Note;
import com.bluezero.phaeton.tasks.LogNoteTask;
import com.bluezero.phaeton.utils.DateTimeUtil;
import com.bluezero.phaeton.utils.FreeTextInputFilter;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.utils.ViewUtil;

public class LogNoteActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.log_note_activity);
        
        super.setHeaderText(R.id.headerTextView, R.string.note_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        InputFilter[] filters = new InputFilter[] { new FreeTextInputFilter() };
        
        ((EditText)findViewById(R.id.detailsEditText)).setFilters(filters);
	}

	public void saveData() {
		EditText detailsEditText = (EditText)findViewById(R.id.detailsEditText);			
		String detail = detailsEditText.getText().toString().trim();
		 				
		if (detail.length() == 0) {
			ViewUtil.setEditTextError(detailsEditText, "You must enter some note content.");					
			return;
		}
		
		String date = DateTimeUtil.getCurrentDateJSONString();
		
		new LogNoteTask(this, this, getChild().Id).execute(new Note(date, detail));
    }
}
