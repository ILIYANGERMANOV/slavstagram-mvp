package com.babushka.slav_squad.analytics.core;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.analytics.event.Event;

import java.util.Map;

import timber.log.Timber;

/**
 * Created by Iliyan on 14.07.17.
 */
class DebugAnalyticsService implements AnalyticsService {
    @Override
    public void logEvent(@NonNull Event event) {
        Timber.d("event_name: %s", event.getName());
        Map<String, Object> params = event.getParams();
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Timber.d("event_param: %s=%s", param.getKey(), param.getValue());
            }
        }
        Timber.d("-------------------------------------------------------------------");
    }
}
