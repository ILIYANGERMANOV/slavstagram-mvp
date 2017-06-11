package com.babushka.slav_squad.util.datetime.periods;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by phyre on 06.02.17.
 */

public class FutureTimePeriod extends TimePeriod {
    public FutureTimePeriod(@NonNull Date date) {
        super(date);
    }

    @Override
    protected void inTheFuture(long millisDifference) {
        initDateTypeAndQuantity(Math.abs(millisDifference));
    }

    @Override
    protected void inThePast(long positiveMillisDifference) {
        //do nothing
    }
}
