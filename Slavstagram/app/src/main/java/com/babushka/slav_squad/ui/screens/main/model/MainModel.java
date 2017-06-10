package com.babushka.slav_squad.ui.screens.main.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.listeners.PostsListener;
import com.babushka.slav_squad.ui.screens.BasePostsModel;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 02.06.17.
 */

public class MainModel extends BasePostsModel implements MainContract.Model {

    public MainModel(@NonNull FirebaseUser user) {
        super(user.getUid());
    }

    @Override
    public void addPostsListener(@NonNull final PostsListener postsListener) {
        Database database = Database.getInstance();
        database.addPostsListener(buildIsLikedPostTransformator(postsListener));
    }


    @Override
    public void removePostsListener() {
        Database.getInstance().removePostsListener();
    }
}
