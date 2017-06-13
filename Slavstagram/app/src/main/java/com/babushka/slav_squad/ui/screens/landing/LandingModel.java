package com.babushka.slav_squad.ui.screens.landing;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public interface LandingModel {
    void saveUser(@NonNull FirebaseUser user);
}
