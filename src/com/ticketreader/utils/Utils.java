package com.ticketreader.utils;

public final class Utils {
	public static byte[] image = null;
	
	public static boolean isInRange(float max, float min, float value) {
		return value < max && value > min;
	}
	
	public static boolean equalsAll(boolean[] array, boolean element) {
		for (boolean item : array) {
			if (item != element)
				return false;
		}
		return true;
	}
}
