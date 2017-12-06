package com.babushka.slav_squad.ui.screens.profile;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.PostsListener;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.screens.PostsView;

/**
 * Created by iliyan on 07.06.17.
 */

public interface ProfileContract {
    interface View extends PostsView {
        void displayUser(@NonNull String highResPhotoUrl, @NonNull String displayName);

        void showEditMode();

        void openEditProfileScreen(int requestCode);
    }

    interface Presenter extends BasePresenter {
        void setupUI();

        void displayUser();

        void displayUserPosts();

        void handleEditProfileClick();

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {
        void addUserPostsListener(@NonNull PostsListener postsListener);

        void removeUserPostsListener();
    }
}
