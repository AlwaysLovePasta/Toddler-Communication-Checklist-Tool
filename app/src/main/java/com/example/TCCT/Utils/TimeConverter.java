package com.example.TCCT.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeConverter {

    private final String date, dateFormat;
    static long years, months, days;

    public TimeConverter(String date, String dateFormat) {
        this.date = date;
        this.dateFormat = dateFormat;
        differenceTime();
    }

    public void differenceTime() {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
           Date pastData = format.parse(date);
           Date currentData = new Date(System.currentTimeMillis());

           Calendar calendar = Calendar.getInstance();
           calendar.setTimeInMillis(currentData.getTime() - pastData.getTime());
           years = calendar.get(Calendar.YEAR)-1970;
           months = calendar.get(Calendar.MONTH);
           days = calendar.get(Calendar.DAY_OF_MONTH)-1;

        } catch (Exception e) {
            Log.e("Log", e.getMessage());
        }
    }

    public String getMonths(){
        months += years*12;
        return Long.toString(months);
    }

    public String getDays(){
        return Long.toString(days);
    }
}
