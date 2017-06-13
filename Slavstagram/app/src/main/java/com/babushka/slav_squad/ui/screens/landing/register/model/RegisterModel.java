package com.babushka.slav_squad.ui.screens.landing.register.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.screens.landing.register.RegisterContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterModel implements RegisterContract.Model {
    @Override
    public void saveUser(@NonNull FirebaseUser user) {
        Database.getInstance().saveUser(user.getUid(), new User(user));
    }
}
