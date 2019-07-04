package com.cloudcoding.CommonLib;

import android.util.Log;


import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve.yang on 5/10/16.
 */
public class DateLib {

    public static boolean isInSameWeekAsDate(DateTime dateTime, DateTime dateTime1) {

        //todo isOnSameDayAsDate
        int d = dateTime.withZone(DateLib.getCurrentDateTimeZone()).getDayOfWeek();
        int d1 = dateTime1.withZone(DateLib.getCurrentDateTimeZone()).getDayOfWeek();

        DateTime sd = dateTime.minusDays(d-1);
        DateTime sd1 = dateTime1.minusDays(d1-1);
        if(DateLib.isSameLocalDay(sd,sd1)){
            return true;
        }

        return false;
    }



    public static boolean isBeforeDateInLocalTime(DateTime dateTime, DateTime dateTime1) {
        if(dateTime1 == null){
            return false;
        }

        int y = dateTime.withZone(DateLib.getCurrentDateTimeZone()).getYear();
        int m = dateTime.withZone(DateLib.getCurrentDateTimeZone()).getMonthOfYear();
        int d = dateTime.withZone(DateLib.getCurrentDateTimeZone()).getDayOfMonth();

        int y1 = dateTime1.withZone(DateLib.getCurrentDateTimeZone()).getYear();
        int m1 = dateTime1.withZone(DateLib.getCurrentDateTimeZone()).getMonthOfYear();
        int d1 = dateTime1.withZone(DateLib.getCurrentDateTimeZone()).getDayOfMonth();

        int dd = d + m*100 + y * 100 * 100;
        int dd1 = d1 + m1*100 + y1 * 100 * 100;
        return (dd < dd1);
    }

    public static String dateToLocalDayStringShortYearWithSlash(DateTime dateTime) {
        if(null == dateTime) {
            return "";
        } else {
            DateTimeFormatter mDateFormat = DateTimeFormat.forPattern("dd/MM/yy");
            String s = dateTime.withZone(DateLib.getCurrentDateTimeZone()).toString(mDateFormat);
            return s;
        }
    }

    public static DateTimeZone getCurrentDateTimeZone(){
        TimeZone timeZone = TimeZone.getDefault();
        DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(timeZone);
        return dateTimeZone;
    }
    public static boolean isBeforeByDay(String s1, String s2) {

        DateTime dateTime = DateLib.dateStringToDateTime(s1);
        DateTime dateTime1 = DateLib.dateStringToDateTime(s2);

        int d = dateTime.getDayOfMonth();
        int m = dateTime.getMonthOfYear();
        int y = dateTime.getYear();


        int d1 = dateTime1.getDayOfMonth();
        int m1 = dateTime1.getMonthOfYear();
        int y1 = dateTime1.getYear();

        boolean bf = dateTime.isBefore(dateTime1);
        boolean sameDay = (d==d1 && m==m1 && y==y1);

        if(bf && !sameDay){
            return true;
        }

        return false;
    }

    //if order = 0: 0:equal,1:first parameter is after second parameter,-1:before
    //If order = 1: reverse
    public static int compareDateTime(String s1, String s2, int order){

        DateTime dateTime = DateLib.dateStringToDateTime(s1);
        DateTime dateTime1 = DateLib.dateStringToDateTime(s2);

        return compareDateTime(dateTime,dateTime1,order);

    }

    //0:equal,1:dateTime is after dateTime1,-1:before
    public static int compareDateTime(DateTime dateTime, DateTime dateTime1){

        return compareDateTime(dateTime,dateTime1,0);
    }
    //if order = 0: 0:equal,1:first parameter is after second parameter,-1:before
    //If order = 1: reverse
    public static int compareDateTime(DateTime dateTime, DateTime dateTime1, int order){

        if(dateTime != null && dateTime1 != null){
            if(order==0){
                return dateTime.compareTo(dateTime1);
            }else{
                return dateTime1.compareTo(dateTime);
            }
        }
        else if(null==dateTime && dateTime1!=null){
            return 1;
        }
        else if(dateTime!=null && dateTime1==null){
            return -1;
        }else{
            return 0;
        }
    }

    public static int hourDiffOfDateTime(DateTime dateTime, DateTime dateTime1){

        long mil = dateTime.getMillis();
        long mil1 = dateTime1.getMillis();

        long diff = mil1 - mil;

        long hour = diff / 1000;
        hour = hour / 60;
        hour = hour / 60;
        return (int)hour;
    }

    public static int minuteDiffOfDateTime(DateTime dateTime, DateTime dateTime1){

        long mil = dateTime.getMillis();
        long mil1 = dateTime1.getMillis();

        long diff = mil1 - mil;

        long minute = diff / 1000;
        minute = minute / 60;
        minute = minute % 60;

        return (int)minute;
    }


    public static DateTime getCurrentDate() {

        return DateTime.now();
    }

    public static String  getCurrentDateString() {

        DateTime dateTime = DateTime.now();

        return dateToString(dateTime);
    }


    public static DateTime calculateStartOfWeekFromDate(DateTime dateTime){

        if (dateTime.getDayOfWeek() != DateTimeConstants.MONDAY){
            return dateTime.minusDays(dateTime.getDayOfWeek() - 1).withTimeAtStartOfDay();
        } else {
            return dateTime.withTimeAtStartOfDay();
        }

    }

    public static String getKeyForCacheFromDate(DateTime dateTime){
        DateTime startOfWeek = calculateStartOfWeekFromDate(dateTime);
        return dateToDayKey(startOfWeek);
    }

    public static int getHoursOffset() {

        TimeZone tz = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        int offsetInMillis = tz.getOffset(calendar.getTimeInMillis());

        int h = offsetInMillis / 3600000;

        return h;
    }

    static public int getMinuteOffset(){

        TimeZone tz = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        int offsetInMillis = tz.getOffset(calendar.getTimeInMillis());

        int h = offsetInMillis / 3600000;
        int m = (offsetInMillis%3600000) / 60000;

        return m;
    }


    //Strings to DateTime
    public static String dateStringToDayStringWithSlash(String s){
        DateTime dateTime = dateStringToDateTime(s);

        return  dateToLocalDayStringWithSlash(dateTime);
    }
    /**
     * Converts a date in format /(Date)123978384700+1100/ into a date string
     * including the timezone as an offset
     *
     *
     * @param dateString
     * @return
     */
    public static DateTime dateStringToDateTime(String dateString){

        if(dateString == null || dateString.length()==0){
            return null;
        }

        final Pattern pattern = Pattern.compile("\\/Date\\((\\d+)([-+]\\d+)?\\)\\/");
        final Matcher matcher = pattern.matcher(dateString);

        if (!matcher.matches()) {
            Log.i("DEV9", "Bad pattern!");
            return null;
        }

        final long millis = Long.parseLong(matcher.group(1));

        String tz = matcher.group(2);

        boolean isFromWebService = true;

        if (tz == null || tz.isEmpty()) { // It can happen, in which case the default is assumed to be...
            tz = "+0000";
            isFromWebService = false;
        }
        DateTimeZone dtz = DateTimeZone.forID(tz);
        DateTime date = new DateTime(millis, dtz);


        /**The webservice can't accept timezone on the epoch string so
         * each date is sent to the server with the timezone already accounted
         * for. So using dates that aren't sent yet have to be modified back to
         * the correct timezone, otherwise every time you read a string date you will
         * lose the offset hours (e.g. GMT-11). So we remove the offset hours if the date
         * did not come from the webservice (a.k.a. doesn't have a +)
         */

        //if the timezone does NOT contain a +, mean it's not back from server
        if (!dateString.contains("+")){
            DateTime date1 = date.minusHours(getHoursOffset());
            Log.i("DEV9", "adjusting user-set time");
            return date1;
        }
        else {
            Log.i("DEV9", "time string from server");
            return date.withZoneRetainFields(DateLib.getCurrentDateTimeZone());
        }
    }

    //DateTime to strings
//    public static String getMonthString(int month) {
//        return new DateFormatSymbols().getMonths()[month-1];
//    }
//    public static String getWeekDayFromInt(int day){
//
//        String[] list = MyApplication.getContext().getResources().getStringArray(R.array.week_days);
//
//        int index = day - 1;
//
//        if(index < 0 || index >= list.length){
//            return "";
//        }
//
//        return list[index];
//
//    }

    public static String dateToString(DateTime dateTime){

        DateTime vDateTime = dateTime.toDateTime(DateTimeZone.UTC);
        int h = getHoursOffset();
        int m = getMinuteOffset();
        DateTime dateTime1 = vDateTime.plusHours(h).plusMinutes(m);

        Log.i("DEV9", "DatetimeToString(): " + dateTime1.toString());

        Instant instant = new Instant(dateTime1);
        long millis = instant.getMillis();
        return "/Date(" + millis + ")/";
    }

    public static String dateToLocalDayString(DateTime dateTime){

        DateTimeFormatter mDateFormat = DateTimeFormat.forPattern("dd-MM-yyyy");

        String s = dateTime.withZone(DateLib.getCurrentDateTimeZone()).toString(mDateFormat);
        return s;
    }

    public static boolean isCurrentDay(DateTime dateTime){
        DateTime currentDate = DateLib.getCurrentDate();
        return isSameLocalDay(dateTime,currentDate);
    }
    public static boolean isSameLocalDay(DateTime dateTime, DateTime dateTime1){
        if(null == dateTime){
            return false;
        }

        String s = dateToLocalDayString(dateTime);
        String s1 = dateToLocalDayString(dateTime1);

        return s.equals(s1);
    }

    public static String dateToDayKey(DateTime dateTime){

        return dateToLocalDayString(dateTime);
    }

    public static String dateToUtcDayString(DateTime dateTime){

        DateTimeFormatter mDateFormat = DateTimeFormat.forPattern("dd-MM-yyyy");

        String s = dateTime.withZone(DateTimeZone.UTC).toString(mDateFormat);
        return s;
    }

    public static String dateToLocalDayStringWithWeek(DateTime dateTime){

        DateTimeFormatter mDateFormat = DateTimeFormat.forPattern("EEEE, dd MMM yyyy");
        String s = dateTime.withZone(DateLib.getCurrentDateTimeZone()).toString(mDateFormat);
        return s;
    }
    public static String dateToLocalDayStringWithSlash(DateTime dateTime){
        if(null == dateTime){
            return "";
        }
        DateTimeFormatter mDateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");
        String s = dateTime.withZone(DateLib.getCurrentDateTimeZone()).toString(mDateFormat);
        return s;
    }


    public static String dateStringToReadableLocalDay(String d){

        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("d MMM yyyy");

        DateTime dateTime = DateLib.dateStringToDateTime(d).withZone(DateLib.getCurrentDateTimeZone());

        return dateTime.toString(dateFormat);
    }

}
