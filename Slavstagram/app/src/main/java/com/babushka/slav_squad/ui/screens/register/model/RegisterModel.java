package com.babushka.slav_squad.ui.screens.register.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.ui.screens.landing.LandingModelImpl;
import com.babushka.slav_squad.ui.screens.register.RegisterContract;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterModel extends LandingModelImpl implements RegisterContract.Model {

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
