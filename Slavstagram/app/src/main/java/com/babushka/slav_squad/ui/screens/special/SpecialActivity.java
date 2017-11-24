package com.babushka.slav_squad.ui.screens.special;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.RemoteConfig;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iliyan on 24.11.17.
 */

public class SpecialActivity extends AppCompatActivity {
    @BindView(R.id.special_days_text_view)
    TextView vDaysText;
    @BindView(R.id.special_hours_text_view)
    TextView vHoursText;
    @BindView(R.id.special_minutes_text_view)
    TextView vMinutesText;
    @BindView(R.id.special_message_text_view)
    TextView vMessageText;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, SpecialActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        ButterKnife.bind(this);
        displayTimeLeft();
        displayMessage();
    }

    private void displayTimeLeft() {
        Date now = Calendar.getInstance().getTime();

        long diff = getSpecialDate().getTime() - now.getTime();
        long diffInSeconds = diff / 1000;

        long diffInMinutes = diffInSeconds / 60;
        long diffInHours = diffInMinutes / 60;
        diffInMinutes -= diffInHours * 60;
        long diffInDays = diffInHours / 24;
        diffInHours -= diffInDays * 24;


        vDaysText.setText(String.format("%02dd", diffInDays));
        vHoursText.setText(String.format("%02dh", diffInHours));
        vMinutesText.setText(String.format("%02dm", diffInMinutes));
    }

    private Date getSpecialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 11, 1, 21, 30);
        return calendar.getTime();
    }

    private void displayMessage() {
        vMessageText.setText(RemoteConfig.getInstance().getMessage());
    }
}
