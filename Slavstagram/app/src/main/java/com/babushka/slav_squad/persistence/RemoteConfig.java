package com.babushka.slav_squad.persistence;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.BuildConfig;
import com.babushka.slav_squad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

/**
 * Created by iliyan on 24.11.17.
 */

public class RemoteConfig {
    private static final String ADMINS = "admins";
    private static final String TARGET_EMAIL = "target_email";
    private static final String MESSAGE = "message";
    private static final String SHOW_LOVE = "show_love";

    private static RemoteConfig sInstance;
    @NonNull
    private final FirebaseRemoteConfig mFirebaseRemoteConfig;

    private RemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings.Builder builder = new FirebaseRemoteConfigSettings.Builder();
        if (BuildConfig.DEBUG) {
            builder.setDeveloperModeEnabled(BuildConfig.DEBUG);
        }
        FirebaseRemoteConfigSettings configSettings = builder.build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        fetchValues();
    }

    public static RemoteConfig getInstance() {
        if (sInstance == null) {
            sInstance = new RemoteConfig();
        }
        return sInstance;
    }

    public void fetchValues() {
        mFirebaseRemoteConfig.fetch(30)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }

    @NonNull
    public String getTargetEmail() {
        return mFirebaseRemoteConfig.getString(TARGET_EMAIL);
    }

    public boolean showLove() {
        return mFirebaseRemoteConfig.getBoolean(SHOW_LOVE);
    }

    @NonNull
    public String getMessage() {
        return mFirebaseRemoteConfig.getString(MESSAGE);
    }

    @NonNull
    public List<String> getAdmins() {
        String adminsSerialized = mFirebaseRemoteConfig.getString(ADMINS);
        List<String> admins = new Gson().fromJson(adminsSerialized, new TypeToken<List<String>>() {
        }.getType());
        if (admins == null) {
            return Collections.emptyList();
        }
        return admins;
    }

}
