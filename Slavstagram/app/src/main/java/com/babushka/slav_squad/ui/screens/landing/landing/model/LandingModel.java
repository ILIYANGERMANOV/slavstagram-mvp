package com.babushka.slav_squad.ui.screens.landing.landing.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.screens.landing.landing.LandingContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 28.05.17.
 */

public class LandingModel implements LandingContract.Model {

    @Override
    public void saveUser(@NonNull FirebaseUser user) {
        Database.getInstance().saveUser(user.getUid(), new User(user));
    }
}
