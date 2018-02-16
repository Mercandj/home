package com.mercandalli.core.clock;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClockUtils {

    private static Calendar currentCalendar;
    private static ClockBasicDate clockBasicDate;

    public static ClockBasicDate getCurrentBasicDate() {
        if (currentCalendar == null) {
            currentCalendar = GregorianCalendar.getInstance();
        } else {
            currentCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        if (clockBasicDate == null) {
            clockBasicDate = new ClockBasicDate(currentCalendar);
        } else {
            clockBasicDate.setCalendar(currentCalendar);
        }
        return clockBasicDate;
    }

}
