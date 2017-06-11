package com.babushka.slav_squad.util.datetime.periods;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by phyre on 06.02.17.
 */

public class PastTimePeriod extends TimePeriod {
    public PastTimePeriod(@NonNull Date date) {
        super(date);
    }

    @Override
    protected void inTheFuture(long millisDifference) {
        //do nothing
    }

    @Override
    protected void inThePast(long millisDifference) {
        initDateTypeAndQuantity(millisDifference);
    }
}
