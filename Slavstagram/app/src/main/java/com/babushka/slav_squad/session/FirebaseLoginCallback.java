package com.babushka.slav_squad.session;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 23.05.17.
 */

public interface FirebaseLoginCallback {
    void onSuccess(@NonNull FirebaseUser user);

    void onError(@Nullable Exception exception);
}
