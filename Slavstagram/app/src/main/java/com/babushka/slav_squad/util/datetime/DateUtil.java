package com.babushka.slav_squad.util.datetime;

import java.util.Calendar;

/**
 * Created by iliyan on 28.05.17.
 */

public class DateUtil {
    public static long getTimestampForNow() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
