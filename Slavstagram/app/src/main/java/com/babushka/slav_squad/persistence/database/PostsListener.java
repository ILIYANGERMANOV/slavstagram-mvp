package com.babushka.slav_squad.persistence.database;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.google.firebase.database.DatabaseError;

/**
 * Created by iliyan on 28.05.17.
 */

public interface PostsListener {
    void onPostAdded(@NonNull Post post);

    void onPostChanged(@NonNull Post post);

    void onPostRemoved(@NonNull Post post);

    void onError(@NonNull DatabaseError databaseError);
}
