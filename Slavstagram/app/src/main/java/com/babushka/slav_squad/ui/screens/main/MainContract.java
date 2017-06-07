package com.babushka.slav_squad.ui.screens.main;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.screens.PostsView;

/**
 * Created by iliyan on 29.05.17.
 */

public interface MainContract {
    interface View extends PostsView {
    }

    interface Presenter extends BasePresenter {
        void displayAllPostsInRealTime();
    }

    interface Model {
        void addPostsListener(@NonNull PostsListener postsListener);

        void removePostsListener();
    }
}
