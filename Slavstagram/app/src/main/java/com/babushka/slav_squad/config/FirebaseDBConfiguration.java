package com.babushka.slav_squad.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by iliyan on 28.05.17.
 */

public class FirebaseDBConfiguration implements Configuration {
    @Override
    public void onConfigure(@NonNull Context context) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
