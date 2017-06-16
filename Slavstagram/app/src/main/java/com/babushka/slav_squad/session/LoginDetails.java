package com.babushka.slav_squad.session;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by iliyan on 23.05.17.
 */

public class LoginDetails {
    @NonNull
    private final String mEmail;
    @NonNull
    private final String mPassword;

    public LoginDetails(@NonNull String email, @NonNull String password) {

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
        return isValidEmail() && !TextUtils.isEmpty(mPassword);
    }

    private boolean isValidEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches();
    }

}
