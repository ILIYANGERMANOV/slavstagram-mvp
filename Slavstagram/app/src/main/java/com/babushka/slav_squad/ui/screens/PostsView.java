package com.babushka.slav_squad.ui.screens;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;

/**
 * Created by iliyan on 07.06.17.
 */

public interface PostsView {
    void addPostAsFirst(@NonNull Post post);

    void updatePost(@NonNull Post post);

    void removePost(@NonNull Post post);
}
