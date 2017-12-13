package com.babushka.slav_squad.ui;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.analytics.SimpleAnalytics;

/**
 * Created by iliyan on 13.12.17.
 */

public class AnalyticsPresenter {
    protected final SimpleAnalytics mAnalytics;

    public AnalyticsPresenter(@NonNull SimpleAnalytics analytics) {
        mAnalytics = analytics;
    }
}
