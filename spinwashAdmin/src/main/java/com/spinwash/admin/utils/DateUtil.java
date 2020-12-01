package com.spinwash.admin.utils;


import android.content.Context;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {
	
	public static final String DEFAULT_DATE_WITH_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss aaa";
	public static final String DEFAULT_TIME_PATTERN = "hh:mm:ss aaa";
	public static final String DEFAULT_TIME_PATTERN_MILITARY = "kk:mm:ss";
	public static final String SIMPLE_DATE_WITH_TIME_PATTERN = "MM/dd/yyyy h:mm a";
	public static final String DATE_START_DATE = "dd/MM/yyyy";
	public static final String DATE_START_DATETIME = "dd/MM/yyyy h:mm a";
	public static final String DATE_MESSAGE = "dd MMM h:mm a";
	
	public static final String DATE_SQL = "yyyy-MM-dd";
	public static final String DATETIME_SQL = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_WITH_MONTH_TIME_PATTERN = "MMM dd, yyyy";
	public static final String DATE_NAME_PATTERN = "EEEE";
	public static final String DATE_WITH_MONTH_TIME_PATTERN_SOLISTATION = "yyyy-MM-dd'T'kk:mm:ss-HH:mm";
	public static final String DATE_WITH_MONTH_TIME_PATTERN_CONTARCTS = "yyyy-MM-dd'T'kk:mm:ss.SSS-HH:mm";
	public static final String UTC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final String DRAFT_DATE_TIME_PATTERN = "MMM dd.yyyy hh:mm a";
	
	public static final String UTC_DATE_PATTERN_INSTRUMENTATION = "yyyy-MM-dd'T'HH:mm:ss.SSS";//yyyy-MM-dd'T'HH:mm:ssZ
	
	public static final String MONTH_PATTERN = "dd MMM yy";
	public static final String FULL_MONTH_PATTERN = "dd MMMM yy";
	public static final String MONTH_PATTERN_SHORT = "MMM dd";
	public static final String MONTH_PATTERN_SHORT_NO = "MMdd";

	public static final String MONTH_DATE_PATTERN = "MMMM dd";
	public static final String MONTH_DAY_YEAR = "MMM dd, yyyy";
	public static final String TIME_12HRS_SHORT = "hh:mm a";
	public static final String TIME_24HRS_LONG = "HH:mm:ss";
	public static final String TIME_24HRS_SHORT = "H:mm";
	
	public static final String MONTH_DATE_TIME_PATTERN_SHORT = "MMM dd hh:mm a";
	public static final List<Long> times = Arrays.asList(
	        TimeUnit.DAYS.toMillis(365),
	        TimeUnit.DAYS.toMillis(30),
	        TimeUnit.DAYS.toMillis(1),
	        TimeUnit.HOURS.toMillis(1),
	        TimeUnit.MINUTES.toMillis(1),
	        TimeUnit.SECONDS.toMillis(1) );
	public static final List<String> timesString = Arrays.asList("year","month","day","hour","minute","second");

	public static Calendar stringToDateOnlyCalendar(String dateString, String pattern){
    	Date date = stringToDate(dateString, pattern);
    	Calendar calendar = ZCalendarDate.getInstance();
    	calendar.setTime(date);
		return calendar;
    }
	public static Date convertGmt(Date source)
	{
	    int rawOffset = TimeZone.getDefault().getRawOffset();

	    Date dest = new Date(source.getTime() + rawOffset);

	    return dest;
	}
	public static Calendar resetTime(Calendar calendar){
        Calendar cal = calendar;
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE,      cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND,      cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
        return cal;
	}

    public static final Date stringToDate (String dateString, String pattern){
    	Date parsedDate = null;
    	try {
    		if(dateString!=null){
    			parsedDate = new SimpleDateFormat(pattern).parse(dateString);
    		}
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		return parsedDate;
    }

    public static final String dateToString(Date date, String pattern) {
    	String convertedDate = null;
    	if(date!=null){
    		convertedDate = (new SimpleDateFormat(pattern)).format(date);
    	}else{
    		convertedDate = "";
    	}
        return convertedDate;
    }
    
    public static final String dateToString(Date date, String pattern,String timeZone) {
    	String convertedDate = null;
    	if(date!=null){
    		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.UTC_DATE_PATTERN_INSTRUMENTATION);
    		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
    		convertedDate = sdf.format(date);
    	}else{
    		convertedDate = "";
    	}
        return convertedDate;
    }
    public static final String changeDateFormat(String dateString , String fromFormat, String toFormat){
    	Date parsedDate = null;
    	try {
    		if(dateString!=null){
    			parsedDate = new SimpleDateFormat(fromFormat).parse(dateString);
    			String convertedDate = null;
    	    	if(parsedDate!=null){
    	    		convertedDate = (new SimpleDateFormat(toFormat)).format(parsedDate);
    	    	}else{
    	    		convertedDate = "";
    	    	}
    	        return convertedDate;
    		}
    		
		} catch (ParseException e) {
			e.printStackTrace();
		}	
    	return null;
    }
    
    public static String millisecondsToDate(long timestampInMiillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampInMiillis);
        String date=formatter.format(calendar.getTime());
        return date;
    }
    
    /**
     * function to check whether the current device date is in between the given two dates 
     * @param startDate -  date1  
     * @param endDate - date2
     * @return returns true if current  date is in between the given two dates 
     */
    public static boolean isInBeetweenDates(Date startDate , Date endDate){
    	Calendar cal= Calendar.getInstance();
		cal.setTime(new Date()); // current date 
		Calendar date1 = Calendar.getInstance();
		Calendar date2 =  Calendar.getInstance();
		date1.setTime(startDate);
		date2.setTime(endDate);
		
		if(cal.after(date1)&&cal.before(date2)){
			return true;
		}else if(isToday(date1)){
			return true;
		}else if(isToday(date2)){
			return true;
		}else{
			return false;
		}
    }
    

    public static Date convertToServerTime(Date datetime){
    	Date lastEvntDate = null;
    	Calendar c = Calendar.getInstance();
        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();
         if (z.inDaylightTime(new Date())) {
            offset = offset + z.getDSTSavings();
        }
        int offsetHrs = offset / 1000 / 60 / 60;
        int offsetMins = offset / 1000 / 60 % 60;

     
        
        c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
        c.add(Calendar.MINUTE, (-offsetMins));
        
        Calendar date1 = Calendar.getInstance();
		date1.setTime(datetime);
        date1.add(Calendar.HOUR,offsetHrs);
        date1.add(Calendar.MINUTE,offsetMins);     
        lastEvntDate = date1.getTime();
        return lastEvntDate;
    }
    
    public static String defaultFormatDate(Date date, Context context, boolean isDate){
    	String formattedDateTime = "";
    	if(null == date){
    		return "";
    	}

    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	if(isDate){

    		formattedDateTime = dateToString(date, DATE_START_DATE);

    	}else{


    		if(DateFormat.is24HourFormat(context)){
    			formattedDateTime = dateToString(date, DEFAULT_TIME_PATTERN_MILITARY);
        	}else{
        		formattedDateTime = dateToString(date, DEFAULT_TIME_PATTERN);
        	}
    	}

    	return formattedDateTime;
    }

    public static String formatDate(Date date,Context context){

    	String formattedDate = null;
    	String formattedTime = null;

    	//Setting hour, minutes and seconds to zero
    	Calendar cal = Calendar.getInstance();
    	if(null == date){
    		return "";
    	}
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE,0);
		today.set(Calendar.SECOND,0);


    	if(!DateFormat.is24HourFormat(context)){
			//Format Time
			 formattedTime = formatTime(date);
    	}else{
    		formattedTime = militaryTime(date);
    	}
    	if(isToday(cal)){
			formattedDate = "Today at " + formattedTime;
		}else if(daysDiff(today.getTime(), cal.getTime())==-1){
			formattedDate = "Yesterday at " + formattedTime;
		}else{
			formattedDate = DateUtil.dateToString(date, DateUtil.DATE_WITH_MONTH_TIME_PATTERN) +" at " + formattedTime;
		}

		return formattedDate;

	}
    private static String militaryTime(Date date){
    	String formattedTime ="";
    	String minuteString = "";
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		if(minutes <= 9){
			minuteString= "0"+(minutes);
		}else{
			minuteString = minutes+"";
		}
		formattedTime = hour+":"+minuteString;
		return formattedTime;
    }

    public static String formatTime(Date date){
    	String formattedTime ="";
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR);
		int minutes = cal.get(Calendar.MINUTE);
		String minuteString = ""+minutes;
		if(minutes <= 9){
			minuteString= "0"+(minutes);
		}
		String amPm = "";
		if(cal.get(Calendar.AM_PM)==0){
			amPm = " AM";
		}else{
			amPm = " PM";
			if(hour==0){
				hour=12;
			}
		}
    	formattedTime = hour+":"+minuteString+amPm;
    	return formattedTime;
    }
    
    public static String getFormattedTime(int hour,int minute){
    	String time = null;
    	String minutes = null;
    	if(minute >= 0 && minute <10){
    		minutes = "0"+minute;
    	}else{
    		minutes = ""+minute;
    	}
    	if(hour>12){
    		hour = hour - 12;
    		time = hour+":"+minutes+"PM";
    	}else if(hour<12){
    		time = hour+":"+minutes+"AM";
    	}else if(hour == 12){
    		time = hour+":"+minutes+"PM";
    	}
    
    	return time;
    }
    
    public static String comparewithCurrentDate(Date date){

    	Calendar cal1 = Calendar.getInstance();
    	String days = "";
		cal1.setTime(cal1.getTime());
		int hour1 = cal1.get(Calendar.HOUR);
		int minute1 = cal1.get(Calendar.MINUTE);
		int sec1 = cal1.get(Calendar.SECOND);
		
    	Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		int hour2 = cal2.get(Calendar.HOUR);
		int minutes2 = cal2.get(Calendar.MINUTE);
		int sec2 = cal2.get(Calendar.SECOND);
		
		long diff = cal1.getTimeInMillis() - cal2.getTimeInMillis();
		long day = diff / (24*60*60*1000);
		long month = diff / (30*24*60*60*1000);
		long years = diff / (365*24*60*60*1000);

		int secDiff = Math.abs(sec2-sec1);
		int minDiff = Math.abs(minutes2-minute1);
		int hrsDiff = Math.abs(hour2-hour1);
		
		if(years>0) {
			if(years==1){
				days = years+" year ago";
			}else{
				days = years+" years ago";
			}
			
		}else if(month>0) {
			if(month==1){
				days = month+" month ago";
			}else{
				days = month+" months ago";
			}
			
		}else if(day>0){
			if(day==1){
				days = day+" day ago";
			}else{
				days = day+" days ago";
			}
			
		}else {			
						
			if(secDiff>0){
				days = secDiff+" secs ";
			}
			if(minDiff>0){
				days = minDiff+" min ";
			}
			if(hrsDiff>0){
				days = hrsDiff+" hours ";
			}
			days+=" ago";
			if(secDiff==0&&minDiff==0&&hrsDiff==0){
				days= "Updated just now";
			}
		}
		return days;
    }
    
    private static long toSeconds(long date) {
    	  return date / 1000L;
    	 }

    	 private static long toMinutes(long date) {
    	  return toSeconds(date) / 60L;
    	 }

    	 private static long toHours(long date) {
    	  return toMinutes(date) / 60L;
    	 }

    	 private static long toDays(long date) {
    	  return toHours(date) / 24L;
    	 }

    	 private static long toMonths(long date) {
    	  return toDays(date) / 30L;
    	 }

    	 private static long toYears(long date) {
    	  return toMonths(date) / 365L;
    	 }

    public static String compareWithCurrentTime(Date date){
    
    	Calendar cal1 = Calendar.getInstance();
    	String days = "";
		cal1.setTime(cal1.getTime());
		int hour1 = cal1.get(Calendar.HOUR);
		int minute1 = cal1.get(Calendar.MINUTE);
		int sec1 = cal1.get(Calendar.SECOND);
		
    	Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		int hour2 = cal2.get(Calendar.HOUR);
		int minutes2 = cal2.get(Calendar.MINUTE);
		int sec2 = cal2.get(Calendar.SECOND);
		
		long diff = cal1.getTimeInMillis() - cal2.getTimeInMillis();
		long day = diff / (24*60*60*1000);

		int secDiff = Math.abs(sec2-sec1);
		int minDiff = Math.abs(minutes2-minute1);
		int hrsDiff = Math.abs(hour2-hour1);
					
		if(day>0){
			if(day==1){
				days = day+" day ago";
			}else{
				days = day+" days ago";
			}
			
		}else {			
						
			if(secDiff>0){
				days = secDiff+" secs ";
			}
			if(minDiff>0){
				days = minDiff+" min ";
			}
			if(hrsDiff>0){
				days = hrsDiff+" hours ";
			}
			days+=" ago";
			if(secDiff==0&&minDiff==0&&hrsDiff==0){
				days= "Updated just now";
			}
		}
		return days;
    }

    public static int countWeeks(Date startDate , Date endDate){
    	int weekCount = 0;
    	boolean isStartWeek = true;

    	Calendar startDateCalender = Calendar.getInstance();
		Calendar endDateCalender = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		Calendar endDateTemp = Calendar.getInstance();

		startDateCalender.setTime(startDate);
		endDateCalender.setTime(endDate);
		endDateTemp.setTime(endDate);

		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		endDateCalender.set(Calendar.HOUR_OF_DAY, 0);
		endDateCalender.set(Calendar.MINUTE,0);
		endDateCalender.set(Calendar.SECOND,0);
		endDateTemp.set(Calendar.HOUR_OF_DAY, 0);
		endDateTemp.set(Calendar.MINUTE,0);
		endDateTemp.set(Calendar.SECOND,0);

		//to check if post was posted on the day course started
		if(endDateCalender.equals(startDateCalender)){
			weekCount = 1;
		}
		// to get the week count
		while(startDateCalender.before(endDateCalender)){
			if(isStartWeek){
				startDateCalender.add(Calendar.DATE, 6);
				isStartWeek = false;
			}else{
				startDateCalender.add(Calendar.DATE, 7);
			}
			if(startDateCalender.after(today)){		// to check if post was posted in this week
				return  -1;
			}
			weekCount++;
		}

		return weekCount;
    }

    public static int dayStatus(Date date ){

    	int dateDiff;

    	Calendar requiredDate = Calendar.getInstance();
    	Calendar today = Calendar.getInstance();
    	requiredDate.setTime(date);

    	today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		requiredDate.set(Calendar.HOUR_OF_DAY, 0);
		requiredDate.set(Calendar.MINUTE, 0);
		requiredDate.set(Calendar.SECOND, 0);

		dateDiff = daysDiff( requiredDate.getTime() , today.getTime() );
		return dateDiff;
    }
    public static int convertPxToDp(int px , float scale ){

    	int dp = (int) (px * scale + 0.5f);

		return dp;
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * <p>Checks if a calendar date is today.</p>
     * @param cal  the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS*60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS*60;
    public final static long DAY_MILLIS = HOUR_MILLIS*24;

    /**
     * Get the days difference
     */
    public static int daysDiff( Date earlierDate, Date laterDate )
    {
        if( earlierDate == null || laterDate == null ) return 0;
        return (int)((laterDate.getTime()/DAY_MILLIS) - (earlierDate.getTime()/DAY_MILLIS));
    }
    public static int daysDiffinday( Date newerDate, Date olderDate )
    {
    	int diffInDays = (int)( (newerDate.getTime() - olderDate.getTime()) 
                / (1000 * 60 * 60 * 24) );
    	return diffInDays;
    }
    
	public static String decimalTimetoHourMinutes(double tTotal) {
		int hours = (int)tTotal;
		double minutesDecimal = ((tTotal - hours) * 60);
		int minutes = (int)minutesDecimal;
		if(minutes<10) {
			return hours+":0"+minutes;
		}else {
			return hours+":"+minutes;
		}
	
	}
	public static String toDuration(long duration) {

	    StringBuffer res = new StringBuffer();
	    for(int i=0;i< times.size(); i++) {
	        Long current = times.get(i);
	        long temp = duration/current;
	        if(temp>0) {
	            res.append(temp).append(" ").append(timesString.get(i) ).append(temp > 1 ? "s" : "").append(" ago");
	            break;
	        }
	    }
	    if("".equals(res.toString()))
	        return "0 second ago";
	    else
	        return res.toString();
	}
    public static final String getCurrentDateTimeString(){
    	String currentDateString = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(new Date());
		String currentTimeString = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(new Date());
		String dateTimeString = currentDateString+" "+currentTimeString;
		
		return dateTimeString;
    }


    
}
