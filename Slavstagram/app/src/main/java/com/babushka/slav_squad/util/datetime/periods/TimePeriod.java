package com.babushka.slav_squad.util.datetime.periods;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.util.datetime.DateType;
import com.babushka.slav_squad.util.datetime.DatetimeConstants;

import java.util.Date;

/**
 * Created by phyre on 06.02.17.
 */

public abstract class TimePeriod {
    //TODO: Refactor and improve OO design
    @Nullable
    private DateType mDateType;
    private int mQuantity;
    private boolean mIsInTheFuture;


    TimePeriod(@NonNull Date date) {
        long now = System.currentTimeMillis();
        long millisDifference = now - date.getTime();
        if (millisDifference < 0) {
            mIsInTheFuture = true;
            inTheFuture(millisDifference);
        } else {
            mIsInTheFuture = false;
            inThePast(millisDifference);
        }
    }

    protected abstract void inTheFuture(long millisDifference);

    protected abstract void inThePast(long millisDiferrence);

    void initDateTypeAndQuantity(long positiveMillisDifference) {
        if (positiveMillisDifference < DatetimeConstants.MINUTE_IN_MILLIS) {
            mDateType = DateType.SECONDS;
            mQuantity = (int) (positiveMillisDifference / DatetimeConstants.SECONDS_IN_MILLIS);
        } else if (positiveMillisDifference < DatetimeConstants.HOUR_IN_MILLIS) {
            mDateType = DateType.MINUTES;
            mQuantity = (int) (positiveMillisDifference / DatetimeConstants.MINUTE_IN_MILLIS);
        } else if (positiveMillisDifference < DatetimeConstants.DAY_IN_MILLIS) {
            mDateType = DateType.HOURS;
            mQuantity = (int) (positiveMillisDifference / DatetimeConstants.HOUR_IN_MILLIS);
        } else if (positiveMillisDifference < DatetimeConstants.WEEK_IN_MILLIS) {
            mDateType = DateType.DAYS;
            mQuantity = (int) (positiveMillisDifference / DatetimeConstants.DAY_IN_MILLIS);
        } else if (positiveMillisDifference < DatetimeConstants.MONTH_IN_MILLIS) {
            mDateType = DateType.WEEKS;
            mQuantity = (int) (positiveMillisDifference / DatetimeConstants.WEEK_IN_MILLIS);
        } else {
            mDateType = DateType.MONTHS;
            mQuantity = (int) (positiveMillisDifference / DatetimeConstants.MONTH_IN_MILLIS);
        }
    }

    @Nullable
    public DateType getDateType() {
        return mDateType;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public boolean isInTheFuture() {
        return mIsInTheFuture;
    }
}
