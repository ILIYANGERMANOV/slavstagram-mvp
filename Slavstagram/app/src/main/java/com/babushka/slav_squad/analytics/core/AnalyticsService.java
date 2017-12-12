package com.babushka.slav_squad.analytics.core;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.analytics.event.Event;


/**
 * Created by Iliyan on 14.07.17.
 */
public interface AnalyticsService {
    void logEvent(@NonNull Event event);
}
