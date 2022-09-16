package com.sadiwala.shivam.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


/**
 * This class will return date format for entire app. There should be no other place where date format
 * should be defined as this will take care of localization also.
 */
public class AaryaDateFormats {

    // All vymo date format category
    public static final String DATE_IN_CURRENT_YEAR = "date_in_curr_year";
    public static final String DATE_NOT_IN_CURRENT_YEAR = "date";
    public static final String MEETING_DATE_TIME_IN_CURR_YEAR = "meeting_date_time_in_curr_year";
    public static final String MEETING_DATE_TIME_NOT_IN_CURR_YEAR = "meeting_date_not_in_curr_year";
    public static final String TIME = "time";
    public static final String TIME_RANGE = "time_range";
    public static final String DATE_IN_CHART = "date_in_chart";
    public static final String MONTH_DATE_IN_CAL = "month_day_in_cal";
    public static final String MONTH_YEAR_IN_CAL = "month_year_in_cal";

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static DateFormat getFormatDdMmYyyy() {
        DateFormat FORMAT_DD_MM_YYYY = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        FORMAT_DD_MM_YYYY.setTimeZone(TimeZone.getDefault());
        return FORMAT_DD_MM_YYYY;
    }

    public static DateFormat getFormatddMMMhhmma() {
        DateFormat FORMAT_DD_MM_YYYY_AM_PM = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
        FORMAT_DD_MM_YYYY_AM_PM.setTimeZone(TimeZone.getDefault());
        return FORMAT_DD_MM_YYYY_AM_PM;
    }

    public static SimpleDateFormat getSimpleDateFormatyyyyMMddTHHmmssSSSZ() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());
        return simpleDateFormat;
    }

}
