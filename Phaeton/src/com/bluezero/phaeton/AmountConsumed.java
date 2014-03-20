package com.bluezero.phaeton;

public class AmountConsumed {
	
	public static int getPercentageAmountFromSpinnerSelectedItemIndex(int selectedItemIndex) {
		int amount = -1;
		
		switch (selectedItemIndex) {
			case 0:
				amount = 0;
				break;
			case 1:
				amount = 25;
				break;
			case 2:
				amount = 50;
				break;
			case 3:
				amount = 75;
				break;
			case 4:
				amount = 0;
				break;
		}
		
		return amount;
	}
}
