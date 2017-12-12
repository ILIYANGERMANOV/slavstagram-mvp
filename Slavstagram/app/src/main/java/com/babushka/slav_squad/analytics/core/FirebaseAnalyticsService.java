package com.babushka.slav_squad.analytics.core;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.analytics.event.Event;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

/**
 * Created by Iliyan on 14.07.17.
 */
class FirebaseAnalyticsService implements AnalyticsService {
    @NonNull
    private final FirebaseAnalytics mAnalytics;

    public FirebaseAnalyticsService(@NonNull Application application) {
        mAnalytics = FirebaseAnalytics.getInstance(application);
    }

    @Override
    public void logEvent(@NonNull Event event) {
        mAnalytics.logEvent(event.getName(), transformParamsToBundle(event.getParams()));
    }

    @Nullable
    private Bundle transformParamsToBundle(@Nullable Map<String, Object> params) {
        if (params == null) return null;
        Bundle paramsBundle = new Bundle();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                paramsBundle.putString(entry.getKey(), (String) value);
            }
        }
        return paramsBundle;
    }
}
