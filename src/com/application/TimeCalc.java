package com.application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeCalc {

    public static int differenceMin(String startHour, String startMinute, String endHour, String endMinute) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date startTime = sdf.parse(startHour + ":" + startMinute);
            Date endTime = sdf.parse(endHour + ":" + endMinute);
            long diffMs = 0;
            diffMs = endTime.getTime() - startTime.getTime();
            long diffSec = diffMs / 1000;
            int diffMin = (int) diffSec / 60;
            return diffMin;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Calendar studentTime(String startHour, String startMinute, int minutesPerStudent) {

        Calendar calendar = new GregorianCalendar();

        try {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(startMinute));

            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minutesPerStudent);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
