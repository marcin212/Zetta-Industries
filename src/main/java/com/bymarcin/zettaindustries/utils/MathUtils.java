package com.bymarcin.zettaindustries.utils;

public class MathUtils{
	public static String addSI(long value, String postunit) {
	    int unit = 1000;
	    if (value < unit) return value + " "+postunit;
	    int exp = (int) (Math.log(value) / Math.log(unit));
	    char pre = "kMGTPE".charAt(exp-1);
	    return String.format("%.2f %s%s", value / Math.pow(unit, exp), pre,postunit);
	}
}
