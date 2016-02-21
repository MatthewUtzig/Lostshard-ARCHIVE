package com.lostshard.Core.Utils;

import java.text.DecimalFormat;
import java.util.Locale;

public class DecimalFormater {

	private static final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
	
	public static String format(Number n) {
		return format.format(n);
	}
}
