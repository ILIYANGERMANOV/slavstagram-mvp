package com.babushka.slav_squad.ui.screens.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.util.datetime.DateType;
import com.babushka.slav_squad.util.datetime.periods.PastTimePeriod;

import java.util.Date;

/**
 * Created by iliyan on 24.06.17.
 */

public class TimeAgo {

    @NonNull
    private final Context mContext;
    private final long mTimestamp;

    public TimeAgo(@NonNull Context context, long timestamp) {
        mContext = context;
        mTimestamp = timestamp;
    }

    public void display(@NonNull TextView textView) {
        PastTimePeriod timePeriod = new PastTimePeriod(new Date(mTimestamp));
        DateType dateType = timePeriod.getDateType();
        if (dateType != null) {
            int stringResId;
            switch (dateType) {
                case SECONDS:
                    textView.setText(R.string.just_now);
                    return;
                case MINUTES:
                    stringResId = R.string.minutes_ago;
                    break;
                case HOURS:
                    stringResId = R.string.hours_ago;
                    break;
                case DAYS:
                    stringResId = R.string.days_ago;
                    break;
                case WEEKS:
                    stringResId = R.string.weeks_ago;
                    break;
                case MONTHS:
                    stringResId = R.string.months_ago;
                    break;
                default:
                    //UNKNOWN, do nothing
                    return;
            }
            int quantity = timePeriod.getQuantity();
            String formattedTime = mContext.getString(stringResId, quantity);
            textView.setText(formattedTime);
        }
    }
}
