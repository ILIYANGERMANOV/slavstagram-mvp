package com.babushka.slav_squad;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();
        Set<Configuration> configurations = getsConfigurationSet();
        for (Configuration configuration : configurations) {
            configuration.onConfigure(this);
        }
    }

    private Set<Configuration> getsConfigurationSet() {
        Set<Configuration> configurationSet = new LinkedHashSet<>();
        configurationSet.add(new FirebaseDBConfiguration());
        configurationSet.add(new TimberConfiguration());
        configurationSet.add(new LeakCanaryConfiguration(this));
        return configurationSet;
    }
}
