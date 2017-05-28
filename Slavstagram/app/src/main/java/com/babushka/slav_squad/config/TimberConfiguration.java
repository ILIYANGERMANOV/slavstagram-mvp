package com.babushka.slav_squad.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.BuildConfig;

import timber.log.Timber;

/**
 * Created by iliyan on 28.05.17.
 */

public class TimberConfiguration implements Configuration {
    @Override
    public void onConfigure(@NonNull Context context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
