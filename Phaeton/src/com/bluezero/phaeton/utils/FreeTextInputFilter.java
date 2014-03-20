package com.bluezero.phaeton.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class FreeTextInputFilter implements InputFilter {
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		for (int i = start; i < end; i++) { 
			char c = source.charAt(i);
			
			boolean valid = Character.isLetterOrDigit(c) || 
							Character.isSpaceChar(c) ||
							Character.isWhitespace(c) ||
							(c == '.') ||
							(c == ',') ||
							(c == '\'') ||
							(c == '-') ||
							(c == '+') ||
							(c == '(') ||
							(c == ')') ||
							(c == ')') ||
							(c == '!') ||
							(c == '?');
			
			if (!valid) {
				return "";
			}			          
        }
		
		return null;	
	}
}