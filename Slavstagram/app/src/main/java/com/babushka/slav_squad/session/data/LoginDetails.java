package com.babushka.slav_squad.session.data;

import android.support.annotation.NonNull;

/**
 * Created by iliyan on 23.05.17.
 */

public class LoginDetails {
    public static final int MIN_PASSWORD_LENGTH = 6;
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
        return isValidEmail() && isValidPassword();
    }

    private boolean isValidEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches();
    }

    private boolean isValidPassword() {
        return mPassword.length() >= MIN_PASSWORD_LENGTH;
    }

}
