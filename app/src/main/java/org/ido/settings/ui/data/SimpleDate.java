package org.ido.settings.ui.data;

import com.blankj.utilcode.util.TimeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class SimpleDate implements Serializable {
    private static final SimpleDateFormat datetimeformat = TimeUtils.getSafeDateFormat("yyyy-MM-dd HH:mm");

    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public static SimpleDate getDate(String dateTime){
        SimpleDate simpleDate = new SimpleDate();
        String year = dateTime.substring(0, 4);
        String month = dateTime.substring(5,7);
        String day = dateTime.substring(8,10);
        String hour = dateTime.substring(11,13);
        String minute = dateTime.substring(14,16);

        simpleDate.setYear(Integer.valueOf(year));
        simpleDate.setMonth(Integer.valueOf(month));
        simpleDate.setDay(Integer.valueOf(day));
        simpleDate.setHour(Integer.valueOf(hour));
        simpleDate.setMinute(Integer.valueOf(minute));
        return simpleDate;
    }

    @Override
    public String toString() {
        return "SimpleDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }

    public  boolean equals(SimpleDate date){
        if (date.year != this.year) {
            return false;
        }
        if (date.month != this.month) {
            return false;
        }
        if (date.day != this.day) {
            return false;
        }
        if (date.hour != this.hour) {
            return false;
        }
        if (date.minute != this.minute) {
            return false;
        }
        return true;
    }
}
