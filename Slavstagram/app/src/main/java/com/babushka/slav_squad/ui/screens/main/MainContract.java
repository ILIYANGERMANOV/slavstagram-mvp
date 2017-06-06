package com.babushka.slav_squad.ui.screens.main;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.BaseView;

/**
 * Created by iliyan on 29.05.17.
 */

public interface MainContract {
    interface View extends BaseView {
        void addPostAsFirst(@NonNull Post post);

        void updatePost(@NonNull Post post);

        void removePost(@NonNull Post post);
    }

    interface Presenter extends BasePresenter {
        void displayAllPostsInRealtime();
    }

    interface Model {
        void addPostsListener(@NonNull PostsListener postsListener);

        void removePostsListener();
    }
}
