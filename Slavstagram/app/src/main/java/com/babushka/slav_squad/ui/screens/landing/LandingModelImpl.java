package com.babushka.slav_squad.ui.screens.landing;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.User;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public class LandingModelImpl implements LandingModel {
    @Override
    public void saveUser(@NonNull FirebaseUser user) {
        Database.getInstance().saveUser(new User(user));
    }
}
