package com.babushka.slav_squad.ui.screens.login.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.screens.login.LoginContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 28.05.17.
 */

public class LoginModel implements LoginContract.Model {

    @Override
    public void saveUser(@NonNull FirebaseUser user) {
        Database.getInstance().saveUser(user.getUid(), new User(user));
    }
}
