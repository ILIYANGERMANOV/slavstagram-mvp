package com.babushka.slav_squad.persistence.database.listeners;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;

/**
 * Created by iliyan on 28.05.17.
 */

public interface PostsListener extends DatabaseErrorCallback {
    void onPostAdded(@NonNull Post post);

    void onPostChanged(@NonNull Post post);

    void onPostRemoved(@NonNull Post post);
}
