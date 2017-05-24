package com.babushka.slav_squad.session;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by iliyan on 23.05.17.
 */

public class UserDetails {
    @NonNull
    private final String mEmail;
    @NonNull
    private final String mPassword;

    public UserDetails(@NonNull String email, @NonNull String password) {

        mEmail = email;
        mPassword = password;
    }

    @NonNull
    public String getEmail() {
        return mEmail;
    }

    @NonNull
    public String getPassword() {
        return mPassword;
    }

    public boolean validate() {
        return !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword);
    }
}
