package com.babushka.slav_squad.special_start;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.SharedPrefs;

import java.util.Map;

/**
 * Created by iliyan on 20.11.17.
 */

public class SpecialStart {
    private final static String SPECIAL_START = "launch_special_start";

    @NonNull
    private String mAction;
    @Nullable
    private Map<String, String> mData;

    public SpecialStart(@NonNull String action, @Nullable Map<String, String> data) {
        mAction = action;
        mData = data;
    }

    public static void loadSpecialStart(@NonNull Context context, @NonNull SpecialStart specialStart) {
        new SharedPrefs(context).put(SPECIAL_START, specialStart);
    }

    @Nullable
    public static SpecialStart consumeLoaded(@NonNull Context context) {
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        SpecialStart specialStart = sharedPrefs.get(SPECIAL_START, SpecialStart.class);
        sharedPrefs.put(SPECIAL_START, null);
        return specialStart;
    }

    @NonNull
    public String getAction() {
        return mAction;
    }

    @Nullable
    public Map<String, String> getData() {
        return mData;
    }
}
