package com.babushka.slav_squad.ui.screens.profile;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.screens.PostsView;

/**
 * Created by iliyan on 07.06.17.
 */

public interface ProfileContract {
    interface View extends PostsView {
        void displayUser(@NonNull String imageUrl, @NonNull String displayName);
    }

    interface Presenter extends BasePresenter {
        void displayUserAndLoadHisPosts();
    }

    interface Model {
        void addUserPostsListener(@NonNull PostsListener postsListener);

        void removeUserPostsListener();
    }
}
