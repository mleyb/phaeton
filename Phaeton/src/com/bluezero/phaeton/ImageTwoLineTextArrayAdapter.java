package com.bluezero.phaeton;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ImageTwoLineTextArrayAdapter<T> extends ArrayAdapter<T> {
    private int _listItemLayoutResId;

    public ImageTwoLineTextArrayAdapter(Context context, T[] ts) {
        this(context, android.R.layout.two_line_list_item, ts);
    }

    public ImageTwoLineTextArrayAdapter(Context context, int listItemLayoutResourceId, T[] ts) {
        super(context, listItemLayoutResourceId, ts);
        _listItemLayoutResId = listItemLayoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = convertView;
        if (null == convertView) { 
        	listItemView = inflater.inflate(_listItemLayoutResId, parent, false);
        }

        // The ListItemLayout must use the standard text item IDs.
        TextView itemLineOneView = (TextView)listItemView.findViewById(android.R.id.text1);
        TextView itemLineTwoView = (TextView)listItemView.findViewById(android.R.id.text2);        
        ImageView itemImageView = (ImageView)listItemView.findViewById(R.id.icon);
        
        T t = (T)getItem(position);                
        
        itemLineOneView.setText(lineOneText(t));
        itemLineTwoView.setText(lineTwoText(t));                  
        itemImageView.setImageBitmap(lineImage(t));
        
        return listItemView;
    }

    public abstract String lineOneText(T t);

    public abstract String lineTwoText(T t);
    
    public abstract Bitmap lineImage(T t);
}