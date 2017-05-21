package com.babushka.slav_squad;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by iliyan on 21.05.17.
 */

public class SessionManager {
    @NonNull
    private final FirebaseAuth mAuth;
    private static SessionManager sInstance;

    public static SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

    private SessionManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isLoggedUser() {
        return mAuth.getCurrentUser() != null;
    }
}
