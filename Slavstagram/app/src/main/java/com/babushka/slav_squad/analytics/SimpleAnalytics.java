package com.babushka.slav_squad.analytics;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.analytics.core.AnalyticsService;
import com.babushka.slav_squad.analytics.event.Event;
import com.babushka.slav_squad.analytics.event.EventBuilder;

/**
 * Created by iliyan on 13.12.17.
 */

public class SimpleAnalytics {
    @NonNull
    private final AnalyticsService mAnalyticsService;

    public SimpleAnalytics(@NonNull AnalyticsService analyticsService) {
        mAnalyticsService = analyticsService;
    }

    public void logEvent(@NonNull String eventName) {
        logEvent(EventBuilder.simpleEvent(eventName));
    }

    public void logEvent(@NonNull Event event) {
        mAnalyticsService.logEvent(event);
    }
}
