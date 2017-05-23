package com.babushka.slav_squad.session;

import android.support.annotation.NonNull;

/**
 * Created by iliyan on 23.05.17.
 */

public class UserDetails {
    @NonNull
    private final String mUsername;
    @NonNull
    private final String mPassword;

    public UserDetails(@NonNull String username, @NonNull String password) {

        mUsername = username;
        mPassword = password;
    }

    @NonNull
    public String getUsername() {
        return mUsername;
    }

    @NonNull
    public String getPassword() {
        return mPassword;
    }
}
