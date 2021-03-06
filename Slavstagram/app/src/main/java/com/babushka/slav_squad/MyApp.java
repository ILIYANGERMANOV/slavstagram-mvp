package com.babushka.slav_squad;

import android.app.Application;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.analytics.SimpleAnalytics;
import com.babushka.slav_squad.analytics.core.AnalyticsService;
import com.babushka.slav_squad.analytics.core.AppAnalyticsService;
import com.babushka.slav_squad.analytics.event.Event;
import com.babushka.slav_squad.config.Configuration;
import com.babushka.slav_squad.config.FirebaseDBConfiguration;
import com.babushka.slav_squad.config.LeakCanaryConfiguration;
import com.babushka.slav_squad.config.TimberConfiguration;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by iliyan on 28.05.17.
 */

public class MyApp extends Application {
    private static MyApp sAppInstance;
    private MusicPlayer mMusicPlayer;
    private AnalyticsService mAnalyticsService;
    private SimpleAnalytics mSimpleAnalytics;

    @NonNull
    public static MyApp getInstance() {
        return sAppInstance;
    }

    @NonNull
    public static MusicPlayer getMusicPlayer() {
        return getInstance().mMusicPlayer;
    }

    @NonNull
    public static AnalyticsService getAnalytics() {
        return getInstance().mAnalyticsService;
    }

    @NonNull
    public static SimpleAnalytics getSimpleAnalytics() {
        return getInstance().mSimpleAnalytics;
    }

    public static void logEvent(@NonNull Event event) {
        getAnalytics().logEvent(event);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppInstance = this;
        mMusicPlayer = new MusicPlayer(this);
        Set<Configuration> configurations = getsConfigurationSet();
        for (Configuration configuration : configurations) {
            configuration.onConfigure(this);
        }
        mAnalyticsService = new AppAnalyticsService(this);
        mSimpleAnalytics = new SimpleAnalytics(mAnalyticsService);
    }

    private Set<Configuration> getsConfigurationSet() {
        Set<Configuration> configurationSet = new LinkedHashSet<>();
        configurationSet.add(new FirebaseDBConfiguration());
        configurationSet.add(new TimberConfiguration());
        configurationSet.add(new LeakCanaryConfiguration(this));
        return configurationSet;
    }
}
