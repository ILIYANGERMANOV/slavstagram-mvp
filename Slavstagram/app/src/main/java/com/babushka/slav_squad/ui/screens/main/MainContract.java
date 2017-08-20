package com.babushka.slav_squad.ui.screens.main;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.PostsListener;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.screens.PostsView;

import java.io.File;

/**
 * Created by iliyan on 29.05.17.
 */

public interface MainContract {
    interface View extends PostsView {
        void downloadPostWithPermissionCheck(@NonNull String imageUrl);

        void showDownloadPostLoading();

        void showDownloadPostSuccess();

        void showDownloadPostError();

        void addImageToGallery(@NonNull File imageFile);

        void openUploadPostScreen();

        void promptGuestToLogin();

        void openProfileScreen();

        void fireShareIntent(@NonNull String textToShare);

        void startSplashScreenAsEntry();
    }

    interface Presenter extends BasePresenter {
        void displayAllPostsInRealTime();

        void downloadPost(@NonNull String imageUrl);

        void handleUploadPostClick();

        void handleMyProfileClick();

        void handleShareClick();

        void handleLogoutClick();
    }

    interface Model {
        void addPostsListener(@NonNull PostsListener postsListener);

        void removePostsListener();

        void downloadImageToFile(@NonNull String imageUrl, @NonNull DownloadCallback downloadCallback);

        interface DownloadCallback {
            void onImageDownloaded(@NonNull File imageFile);

            void onError();
        }
    }
}
