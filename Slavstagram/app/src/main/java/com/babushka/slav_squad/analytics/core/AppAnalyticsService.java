package com.babushka.slav_squad.analytics.core;

import android.app.Application;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.BuildConfig;
import com.babushka.slav_squad.analytics.event.Event;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by iliyan on 12.12.17.
 */

public class AppAnalyticsService implements AnalyticsService {
    @NonNull
    private final Application mApp;

    private final Set<AnalyticsService> mAnalyticsServices;

    public AppAnalyticsService(@NonNull Application app) {
        mApp = app;
        mAnalyticsServices = new HashSet<>();
        addAnalyticsServices();
    }

    private void addAnalyticsServices() {
        mAnalyticsServices.add(new FirebaseAnalyticsService(mApp));
        if (BuildConfig.DEBUG) {
            mAnalyticsServices.add(new DebugAnalyticsService());
        }
    }

    @Override
    public void logEvent(@NonNull Event event) {
        for (AnalyticsService analyticsService : mAnalyticsServices) {
            analyticsService.logEvent(event);
        }
    }
}
