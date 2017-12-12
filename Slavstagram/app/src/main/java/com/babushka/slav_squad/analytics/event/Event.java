package com.babushka.slav_squad.analytics.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by Iliyan on 14.07.17.
 */
public class Event {
    @NonNull
    private final String mName;
    @Nullable
    private final Map<String, Object> mParams;

    Event(@NonNull String name, @Nullable Map<String, Object> params) {
        mName = name;
        mParams = params;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @Nullable
    public Map<String, Object> getParams() {
        return mParams;
    }
}
