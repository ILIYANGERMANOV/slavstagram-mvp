package com.babushka.slav_squad.persistence.database;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by iliyan on 25.05.17.
 */

public class Database {
    private static Database sDatabase;
    private final DatabaseReference mDatabase;

    private Database() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static Database getInstance() {
        if (sDatabase == null) {
            sDatabase = new Database();
        }
        return sDatabase;
    }

    public void saveUser(@NonNull User user) {
        mDatabase.child(Table.USERS_TABLE).push().setValue(user);
    }
}
