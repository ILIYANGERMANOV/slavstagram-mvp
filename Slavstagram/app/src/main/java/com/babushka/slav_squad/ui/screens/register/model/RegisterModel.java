package com.babushka.slav_squad.ui.screens.register.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.ui.screens.register.RegisterContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterModel implements RegisterContract.Model {
    @Override
    public void saveUser(@NonNull FirebaseUser user) {
        Database.getInstance().saveUser(user.getUid(), new User(user));
    }

    @NonNull
    @Override
    public String getRandomProfileImageUrl() {
        //TODO: Randomize profile photo
        return "https://firebasestorage.googleapis.com/v0/b/slav-squad.appspot.com/o/image%2Fic_launcher.png?alt=media&token=d545dc7d-3318-4893-8547-c5c5a755731f";
    }

    @Override
    public void uploadProfilePhoto(@NonNull Uri photoUri, @NonNull Storage.UploadImageListener listener) {
        Storage.getInstance().uploadImage(photoUri, listener);
    }
}
