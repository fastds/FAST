package org.fastds.model;

import java.util.Calendar;

public class HelperFunctions {
	
	public static Calendar ConvertFromJulian(int m_JulianDate)
	{
		long J = m_JulianDate + 2400001 + 68569;
	    long C = 4 * J / 146097;
	    J = J - (146097 * C + 3) / 4;
	    long Y = 4000 * (J + 1) / 1461001;
	    J = J - 1461 * Y / 4 + 31;
	    long M = 80 * J / 2447;
	    int Day =(int)( J - 2447 * M / 80);
	    J = M / 11;
	    int Month = (int)(M + 2 - (12 * J));
	    int Year = (int)(100 * (C - 49) + Y + J);
	    Calendar cal = Calendar.getInstance();
	    cal.set(Year, Month, Day);
	    return cal;
	}
}
