package com.babushka.slav_squad.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

/**
 * Created by iliyan on 25.05.17.
 */

public class SharedPrefs {
    public static final String PREFS_NAME = "prefs";
    @NonNull
    private final SharedPreferences mPrefs;

    public SharedPrefs(@NonNull Context context) {
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    public <T> T get(@NonNull String key, Class<T> type) {
        String serializedObject = get(key);
        return new Gson().fromJson(serializedObject, type);
    }

    @Nullable
    public String get(@NonNull String key) {
        return mPrefs.getString(key, null);
    }

    public boolean get(@NonNull String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    public <T> void put(@NonNull String key, @NonNull T value) {
        put(key, new Gson().toJson(value));
    }

    public void put(@NonNull String key, String value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void put(@NonNull String key, boolean value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.clear();
        edit.apply();
    }

}
