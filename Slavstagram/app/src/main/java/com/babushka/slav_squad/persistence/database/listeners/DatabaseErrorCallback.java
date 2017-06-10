package com.babushka.slav_squad.persistence.database.listeners;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseError;

/**
 * Created by iliyan on 10.06.17.
 */

interface DatabaseErrorCallback {
    void onError(@NonNull DatabaseError databaseError);
}
