package com.babushka.slav_squad.config;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by iliyan on 06.06.17.
 */

public class LeakCanaryConfiguration implements Configuration {
    @NonNull
    private final Application mApplication;

    public LeakCanaryConfiguration(@NonNull Application application) {
        mApplication = application;
    }

    @Override
    public void onConfigure(@NonNull Context context) {
        if (LeakCanary.isInAnalyzerProcess(mApplication)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not applyBusinessLogic your app in this process.
            throw new Error("LeakCanary initialize error!");
        }
        LeakCanary.install(mApplication);
    }
}
