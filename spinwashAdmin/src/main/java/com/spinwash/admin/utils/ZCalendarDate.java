/**
 * 
 */
package com.spinwash.admin.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 *
 */
public class ZCalendarDate extends GregorianCalendar {
    
        /**
	 * 
	 */
	private static final long serialVersionUID = -8924125065065025588L;

		public static Calendar getInstance() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE,      cal.getActualMinimum(Calendar.MINUTE));
            cal.set(Calendar.SECOND,      cal.getActualMinimum(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
            return cal;
        }
}
