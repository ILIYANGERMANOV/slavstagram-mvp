package com.babushka.slav_squad.ui.screens.main.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.ui.screens.main.MainContract;

/**
 * Created by iliyan on 02.06.17.
 */

public class MainModel implements MainContract.Model {
    @Override
    public void addPostsListener(@NonNull PostsListener postsListener) {
        Database.getInstance().addPostsListener(postsListener);
    }

    @Override
    public void removePostsListener() {
        Database.getInstance().removePostsListener();
    }
}
