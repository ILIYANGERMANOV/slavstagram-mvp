package com.babushka.slav_squad;

import android.app.Application;

import com.babushka.slav_squad.config.Configuration;
import com.babushka.slav_squad.config.FirebaseDBConfiguration;
import com.babushka.slav_squad.config.TimberConfiguration;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by iliyan on 28.05.17.
 */

public class MyApp extends Application {
    private static final Set<Configuration> sConfigurationSet = new LinkedHashSet<>();

    static {
        sConfigurationSet.add(new FirebaseDBConfiguration());
        sConfigurationSet.add(new TimberConfiguration());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        for (Configuration configuration : sConfigurationSet) {
            configuration.onConfigure(this);
        }
    }
}
