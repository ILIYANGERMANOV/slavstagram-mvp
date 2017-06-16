package com.babushka.slav_squad.session;

import android.support.annotation.NonNull;

/**
 * Created by iliyan on 16.06.17.
 */

public class UserDetails extends LoginDetails {
    @NonNull
    private final String mDisplayName;

    public UserDetails(@NonNull String email, @NonNull String password,
                       @NonNull String displayName) {
        super(email, password);
        mDisplayName = displayName;
    }

    @NonNull
    public String getDisplayName() {
        return mDisplayName;
    }
}
