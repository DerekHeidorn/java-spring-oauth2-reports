package com.example.demo.services.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateUtils;


public class DateUtil {

	public final static String DATE_TIME_HOUR_MIN_FORMAT = "HH:mm";
	public final static String DATE_FORMAT = "dd-MMM-yyyy";
	public final static String DATE_TIME_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	public final static String DATE_TIME_FORMAT_NO_SECS = "dd-MMM-yyyy HH:mm";
	public final static String DATE_FORMAT_UI_LONG = "MMMMMMMMMM d, yyyy";
	public final static String DATE_FORMAT_YUI = "MM/dd/yyyy"; // used for Yahoo UI Javascript libraries
	public final static String DATE_FORMAT_YUI_MONTH = "MM/yyyy"; // used for Yahoo UI Javascript libraries
	public final static SimpleDateFormat dbTimeStampFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	
	
    /**
    *
    * Get a Date object given Year, Month, Day.
    * @param year The year
    * @param month The month (1-12)
    * @param day The Day (1-31)
    * @return Date Date Object
    */
   public static Date getDate(int year, int month, int day) {
       Calendar calendar = Calendar.getInstance();
       calendar.set(year, month - 1, day, 0, 0, 0);
       //fix to make sure we also get 0 for milliseconds, not random milliseconds
       calendar.set(Calendar.MILLISECOND, 0);
       return new Date(calendar.getTimeInMillis());
   }	
	
   public static boolean areDatesNotEqual(Date d1, Date d2) {
	   return !areDatesEqual(d1, d2);
   }
   
    public static boolean areDatesEqual(Date d1, Date d2) {
        GregorianCalendar a = new GregorianCalendar();
        a.setTime(d1);

        GregorianCalendar b = new GregorianCalendar();
        b.setTime(d2);

        if (a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
                && a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
                && a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }
    
    public static boolean isCurrentYearOrFuture(Date d) {
    	if(d != null) {
    		Date now = new Date();
    		
    		if(d.after(now)) {
    			 return true;
    		} else {
    		
		        GregorianCalendar a = new GregorianCalendar();
		        a.setTime(d);
		
		        GregorianCalendar b = new GregorianCalendar();
		        b.setTime(now);
		
		        if (a.get(Calendar.YEAR) == b.get(Calendar.YEAR)) {
		            return true;
		        }
    		}
    	}
        return false;
    }    

    public static Date startDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }
    
    public static Date endDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    public static String getTimeForEta(Date date) {
    	if (date == null) 
    		return null;
    	
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
    	return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }

    /*
     * Start Date is inclusive, End Date is exclusive
     */
    public static boolean doesFridayOrSaturdayWithinDateRange(Date startDate, Date endDate) {
    	boolean ret = false;
    	Date tmpStartDate = (Date) startDate.clone();
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(tmpStartDate);
    	
    	// starting with the startDate, test each day till the endDate
    	while(cal.getTime().before(endDate)) {
    		if(Calendar.FRIDAY == cal.get(Calendar.DAY_OF_WEEK)
    				|| Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK)) {
    			return true;
    		}
    		cal.add(Calendar.DATE, 1);
    	}
    	return ret;
    }    
  
    
    public static String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(DATE_FORMAT);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }


    /**
     * This method generates a string representation of a date based on the System Property
     * 'dateFormat' in the format you specify on input
     *
     * @param aDate
     *            A date to convert
     * @return a string representation of the date
     */
    public static String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }    
    
    /**
     * This method generates a string representation of a date's date/time in the format you specify
     * on input
     *
     * @param aMask
     *            the date pattern the string is in
     * @param aDate
     *            a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            //log.warn("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }    
    
    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return DATE_FORMAT;
    }    
	
    /*
     * Return a new date which is created by adding the amount in amount to the time field specified
     * by field to the passed date. date - the date to add values to field- the field hours,
     * minutes, months as specified by one of the Calendar field constants amount - the amount you
     * want to add
     */
    private static Date addFields(Date date, int field, int amount) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setLenient(false);

        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }    
    
    /*
     * This class adds the specified number of months onto the passed date. Hours, minutes, seconds
     * and millisecond will be the same in the returned date as they were in the passed date. This
     * method has not been validated for timezone independence. It is assumed that the passed date
     * and returned dates are using the same time zone as what the system will default to.
     */
    public static Date addMonths(Date date, int months) {
        return addFields(date, Calendar.MONTH, months);
    }    
    
    /*
     * This class adds the specified number of days onto the passed date. Hours, minutes, seconds
     * and millisecond will be the same in the returned date as they were in the passed date. This
     * method has not been validated for timezone independence. It is assumed that the passed date
     * and returned dates are using the same time zone as what the system will default to.
     */
    public static Date addDays(Date date, int days) {
        return addFields(date, Calendar.DATE, days);
    }    
    
    public static Date resetToBeginningOfTheDay(Date date){
    	Date resetDate = DateUtils.setHours(date, 0);
    	resetDate = DateUtils.setMinutes(resetDate, 0);
    	resetDate = DateUtils.setSeconds(resetDate, 0);
    	resetDate = DateUtils.setMilliseconds(resetDate, 0);
    	
    	return resetDate;
    }
    
    public static Date resetToEndOfTheDay(Date date){
    	Date resetDate = DateUtils.addDays(date, 1);
    	resetDate = resetToBeginningOfTheDay(resetDate);
    	
    	return resetDate;
    }

    public static double round1(double value) {
		return Math.round(value * 10.0) / 10.0;
	}

	public static String getElapsedText(long elapsedMillis) {
		if (elapsedMillis < 60000) {
			double unit = round1(elapsedMillis / 1000.0);
			return unit + (unit == 1 ? " second" : " seconds");
		} else if (elapsedMillis < 60000 * 60) {
			double unit = round1(elapsedMillis / 60000.0);
			return unit + (unit == 1 ? " minute" : " minutes");
		} else if (elapsedMillis < 60000 * 60 * 24) {
			double unit = round1(elapsedMillis / (60000.0 * 60));
			return unit + (unit == 1 ? " hour" : " hours");
		} else {
			double unit = round1(elapsedMillis / (60000.0 * 60 * 24));
			return unit + (unit == 1 ? " day" : " days");
		}
	}
	
	public static boolean isDateBetween(Date searchDate,Date startDate,Date endDate){
		boolean isDateBetween = false;
		if(searchDate.compareTo(startDate) >= 0 &&
		   searchDate.compareTo(endDate) < 0){
			isDateBetween = true;
		}
		return isDateBetween ; 
	}

    //--------------------------------------------------------------------------->
    /**
     * Compares 2 date ranges and returns whether they overlap
     *
     * Start Date is Inclusive and End Date are Exclusive
     *
     * @param d1_StartDt First Date Range Start Date
     * @param d1_EndDt First Date Range End Date, can be null
     * @param d2_StartDt Start Date Range to compare
     * @param d2_EndDt End Date Range to compare, can be null
     *
     * @return boolean
     */
    public static boolean areDateRangesOverlapping(Date d1_StartDt, Date d1_EndDt, Date d2_StartDt, Date d2_EndDt) {

    	// both ranges have to overlap with infinite end dates (null == no end date)
    	
    	// Scenario1    	
    	//  |------ d1 ------------....
    	// |------ d2 ------------....     	
    	if(d1_EndDt == null && d2_EndDt == null) {
    		return true;
    	}

    	
    	if(d1_EndDt != null) {

    		// Scenario2a
    		// |------- d1 --------|
    		//                    |------- d2 --------|
    		// Scenario2b
    		// |------- d1 --------|
    		// |------- d2 -----|    		
    		if(d2_StartDt.before(d1_EndDt) && (d2_StartDt.after(d1_StartDt) || areDatesEqual(d1_StartDt, d2_StartDt))) {
    			return true;
    		}
    		
    		// Scenario3a
    		//    |------- d1 --------|
    		// |------- d2 -----|     
    		// Scenario3b
    		//                 |------- d1 --------|
    		// |------- d2 -----|      		
    		if(d2_EndDt != null) {
    			if(d2_EndDt.before(d1_EndDt) && d2_EndDt.after(d1_StartDt)) {
        			return true;
        		}

        		// Scenario4
        		//  |--- d1 ---|
        		// |------- d2 -----|       			
    			if(d1_StartDt.before(d2_EndDt) && (d1_StartDt.after(d2_StartDt) || areDatesEqual(d1_StartDt, d2_StartDt))) {
        			return true;
        		}    			
    			
    			
    		// Scenario5a
    		// |------- d1 --------|
    		//                    |------- d2 -----...    
    		// Scenario5b
    		//                 |------- d1 --------|
    		// |------- d2 -----...      			
    		} else {

    			if(d2_StartDt.before(d1_EndDt)) {
        			return true;
        		}
    		}    		


    	} else { // endDate1 == null, therefore endDate is infinite

    		// Scenario6a
    		// |------- d1 -----...      		
    		//    |------- d2 --------|
  
    		// Scenario6b
    		//                    |------- d1 -----... 
    		// |------- d2 --------|    		
    		
    		if(d1_StartDt.before(d2_EndDt)) {
    			return true;
    		}

    	}


    	return false;
    }

    /**
     * Is d1 equal or after d2?
     *
     * @param d1
     *            First date used in comparison
     * @param d2
     *            Second date used in comparison
     * @return boolean
     */
    public static boolean isDateEqualOrAfter(Date d1, Date d2) {
    	if(d1.after(d2) || areDatesEqual(d1, d2)) {
    		return true;
    	}
        return false;
    }    
    
    //--------------------------------------------------------------------------->
    /**
     * Is d1 equal or before d2?
     *
     * @param d1
     *            Base date used in comparison
     * @param d2
     *            Date used in comparison to base date
     * @return boolean true if d1 is equal to or before d2.
     */
    public static boolean isDateEqualOrBefore(Date d1, Date d2) {
    	if(d1.before(d2) || areDatesEqual(d1, d2)) {
    		return true;
    	}
        return false;
    }    
    

	
    /*
     * Start Date is Inclusive, End Date is Exclusive
     */
    public static List<Date> createConsecutiveDateListByDay(Date startDt, Date endDt) {
    	List<Date> dates = new ArrayList<Date>();
    	
		Date tmp = startDt;
		do {
			dates.add(tmp); 
			tmp = DateUtils.addDays(tmp, 1);
		} while(tmp.before(endDt));

    	return dates;
    }
    
    public static Map<Integer,String> getMonthsAsMap(){
    	Map<Integer,String> monthsMap = new TreeMap<Integer,String>();
    	String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths();
    	//switch here to DateFormatSymbols(Locale.CANADA_FRENCH)
    	 for (int i = 0; i < months.length-1 ; i++) {
    		 monthsMap.put(i, months[i]);
    	 }
    	 return monthsMap;
    }
    
    public static Date getFirstDateOfYear(int year){
    	Calendar calendar = new GregorianCalendar(year, Calendar.JANUARY, 1);
    	Date date = startDate(calendar.getTime());
    	return date;
    }     
    
    public static Date getFirstDateOfYear(Date d){
    	Calendar calendar = new GregorianCalendar(getYear(d), Calendar.JANUARY, 1);
    	Date date = startDate(calendar.getTime());
    	return date;
    }     
    
    public static Date getFirstDateOfCurrentYear(){
    	Calendar calendar = new GregorianCalendar(getYear(new Date()), Calendar.JANUARY, 1);
    	Date date = startDate(calendar.getTime());
    	return date;
    }     
    
    public static Date getFirstDateOfCurrentMonth(Integer year,Integer month){
    	Calendar calendar = new GregorianCalendar(year, month, 1);
    	Date date = startDate(calendar.getTime());
    	return date;
    } 
    
    public static Date getLastDateOfCurrentMonth(Integer year,Integer month){
    	Calendar calendar = new GregorianCalendar(year, month, 1);
    	calendar.add(Calendar.MONTH, 1) ; // Add One Month
    	calendar.add(Calendar.DAY_OF_MONTH, -1) ; // subtract one day
    	Date date = endDate(calendar.getTime());
    	return date;
    }   
    
    public static Date getLastDateOfYear(int year){ 
    	Calendar calendar = new GregorianCalendar(year, Calendar.DECEMBER, 31);
    	Date date = endDate(calendar.getTime());
    	return date;
    }   
    
    public static Date getLastDateOfYear(Date d){ 
    	Calendar calendar = new GregorianCalendar(getYear(d), Calendar.DECEMBER, 31);
    	Date date = endDate(calendar.getTime());
    	return date;
    }     
    
    public static Date getLastDateOfCurrentYear(){ 
    	Calendar calendar = new GregorianCalendar(getYear(new Date()), Calendar.DECEMBER, 31);
    	Date date = endDate(calendar.getTime());
    	return date;
    }      
    
    public static Date getLastDateOfPreviousMonth(Integer year,Integer month){
    	Calendar calendar = new GregorianCalendar(year, month, 1);
    	calendar.add(Calendar.DAY_OF_MONTH, -1) ;
    	Date date = endDate(calendar.getTime());
    	return date;
    }
    
    public static Date getFirstDateOfNextMonth(Integer year,Integer month){
    	Calendar calendar = new GregorianCalendar(year, month, 1);
    	calendar.add(Calendar.MONTH, 1) ;
    	Date date = startDate(calendar.getTime());
    	return date;
    }

  

    /*
     * Finds the intersect of 2 date ranges.  All dates assumed to be inclusive.
     * 
     * 
     * 
     */
    public static Date[] dateIntersect(Date startDt, Date endDt, Date compareStartDt, Date compareEndDt) {
    	
    	Date intersectingStartDt = null;
    	Date intersectingEndDt = null;
    	

    	// find the start date
    	if(areDatesEqual(startDt, compareStartDt)) {    		
    		intersectingStartDt = (Date) startDt.clone();
    		
    	} else if(startDt.after(compareStartDt)  && startDt.before(compareEndDt)) {
    		intersectingStartDt = (Date) startDt.clone();
    		
    	} else if(startDt.before(compareStartDt) ) {
    		intersectingStartDt = (Date) compareStartDt.clone();
    		
    	} else if (areDatesEqual(startDt, compareEndDt) ){
    		intersectingStartDt = (Date) startDt.clone();
    	}
    	
    	// find the end date
    	if(areDatesEqual(endDt, compareEndDt) ) {    		
    		intersectingEndDt = (Date) endDt.clone();
    		
    	} else if(endDt.before(compareEndDt)  && endDt.after(compareStartDt)) {
    		intersectingEndDt = (Date) endDt.clone();
    		
    	} else if(endDt.after(compareEndDt) ) {
    		intersectingEndDt = (Date) compareEndDt.clone();
    	}    	
    	
    	
    	if(intersectingStartDt != null && intersectingEndDt != null) {
    		return new Date[]{intersectingStartDt, intersectingEndDt};
    	} else {    	
    		return null;
    	}
    }
    
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }    
    
    public static Long getNumberOfDaysBetweenDates(Date d1, Date d2) {
    	final long ONE_HOUR = 60 * 60 * 1000L;
    	if(d1 == null || d2 == null) {
    		return null;
    	}
    	
//    	if(areDatesEqual(d1, d2)) {
//    		return new Long(0);
//    	}
    	
    	Date newD1 = DateUtils.truncate(d1, Calendar.DATE);
    	Date newD2 = DateUtils.truncate(d2, Calendar.DATE);
    	
    	return new Long(((newD2.getTime() - newD1.getTime() + ONE_HOUR) /
                (ONE_HOUR * 24)));
    }    
    
}
