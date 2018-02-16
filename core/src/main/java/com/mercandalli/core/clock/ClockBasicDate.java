package com.mercandalli.core.clock;

import java.util.Calendar;

/**
 * The basic date data.
 */
public class ClockBasicDate {

    /**
     * The {@link Calendar#HOUR_OF_DAY}.
     */
    private int hourOfTheDay;

    /**
     * The {@link Calendar#MINUTE}.
     */
    private int minute;

    /**
     * The {@link Calendar#SECOND}.
     */
    private int second;

    /**
     * A simple constructor
     *
     * @param calendar A {@link Calendar} the initialize the date.
     */
    /* package */
    ClockBasicDate(final Calendar calendar) {
        setCalendar(calendar);
    }

    /**
     * Get the {@link Calendar#HOUR_OF_DAY}.
     *
     * @return The {@link Calendar#HOUR_OF_DAY}.
     */
    public int getHourOfTheDay() {
        return hourOfTheDay;
    }

    /**
     * Get the {@link Calendar#MINUTE} formatted.
     *
     * @return The {@link Calendar#MINUTE} formatted.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Get the {@link Calendar#SECOND} formatted.
     *
     * @return The {@link Calendar#SECOND} formatted.
     */
    public int getSecond() {
        return second;
    }

    /* package */ void setCalendar(final Calendar calendar) {
        hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }
}
