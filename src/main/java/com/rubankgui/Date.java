package com.rubankgui;
/**
 Stores the date as three integer variables, various methods for evaluating dates
 @author Tyler Amalfa, Zafar Khan
 */
public class Date implements Comparable<Date>{
    public static final int[] NONLEAPYEAR = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //days of all 12 months during a non-leap year
    public static final int FEB_LEAPYEAR = 29; //days of February during a leap year
    public static final int QUADRENNIAL = 4; //every four years
    public static final int CENTENNIAL = 100; //every 100 years
    public static final int QUARTERCENTENNIAL = 400; //every 400 years
    private int year;
    private int month;
    private int day;
    /**
     Constructs a type Date from three integers
     @param year holds the value of the year
     @param month holds the value of the month
     @param day holds the value of the day
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     Gets the year as an integer
     @return int year
     */
    public int getYear() {
        return year;
    }
    /**
     Gets the month as an integer
     @return int month
     */
    public int getMonth() {
        return month;
    }
    /**
     Gets the day as an integer
     @return int day
     */
    public int getDay() {
        return day;
    }

    /**
     Provides date in a String format
     @return date as mm/dd/yyyy
     */
    public String toString() {
        return month+"/"+day+"/"+year;
    }

    /**
     Checks if the date is a real valid calendar date
     @return true if year>0, month is in range [1,12], and if month=2 day must be within range based on leap year rules
     */
    public boolean isValid() {
        if(year<=0) return false;
        if((month<=0)||(month>12)) return false;
        int dayLimit = NONLEAPYEAR[month-1];
        if((month==2)&&(((year % 4 == 0)&&(year % 100 != 0))
                ||((year % 4 == 0)&&(year % 100 != 0)&&(year % 400 == 0)))) {
            dayLimit = FEB_LEAPYEAR;
        }
        return (day<=dayLimit)&&(day>0);
    }
    /**
     Compares date to inputted date
     @param date the date to be compared.
     @return positive integer if this.date greater than date, zero if same date, negative integer if this.date less than date
     */
    public int compareTo(Date date) {
        int compare = this.year - date.getYear();
        if(compare == 0) {
            compare = this.month - date.getMonth();
            if(compare == 0) return this.day-date.getDay();
            return compare;
        }
        return compare;
    }
}
