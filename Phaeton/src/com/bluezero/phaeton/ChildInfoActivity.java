package com.bluezero.phaeton;

import java.util.Date;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluezero.phaeton.models.Child;
import com.bluezero.phaeton.utils.ISO8601;
import com.bluezero.phaeton.utils.IntentUtil;

public class ChildInfoActivity extends PhaetonActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.show_child_info_activity);   
        
        super.setHeaderText(R.id.headerTextView, R.string.child_info_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Child child = IntentUtil.getChildFromIntent(getIntent());
                       
        Date dateOfBirth = ISO8601.parseISO8601String(child.DateOfBirth);
              
        ((TextView)findViewById(R.id.nameTextView)).setText(child.Forename + " " + child.Surname);        
        ((TextView)findViewById(R.id.notesTextView)).setText(child.Notes.isEmpty() ? "None" : child.Notes);
        ((TextView)findViewById(R.id.dateOfBirthTextView)).setText(DateFormat.format("MMMM dd yyyy", dateOfBirth));
        
        Bitmap image = child.getImage();
        if (image != null) {
        	ImageView imageView = (ImageView)findViewById(R.id.pictureImageView); 
        	imageView.setImageBitmap(image);
        }
	}
}
