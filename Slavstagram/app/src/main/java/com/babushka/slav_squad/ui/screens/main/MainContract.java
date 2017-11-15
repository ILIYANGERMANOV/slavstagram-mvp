package com.babushka.slav_squad.ui.screens.main;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.PostsListener;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.PostsView;

import java.io.File;

/**
 * Created by iliyan on 29.05.17.
 */

public interface MainContract {
    interface View extends PostsView {
        void displayUserProfilePicture(@NonNull String url);

        void displayUserName(@NonNull String displayName);

        void downloadPostWithPermissionCheck(@NonNull String imageUrl);

        void showDownloadPostLoading();

        void showDownloadPostSuccess();

        void showDownloadPostError();

        void addImageToGallery(@NonNull File imageFile);

        void showUploadPostLayout();

        void hideUploadPostLayout();

        void uploadPostViaCamera(int requestCode);

        void openGalleryWithCheck(int requestCode);

        void openUploadPostScreen(int requestCode, @NonNull Uri selectedImage);

        void promptGuestToLogin();

        void openProfileScreen();

        void openAboutScreen();

        void fireShareIntent(@NonNull String textToShare);

        void startSplashScreenAsEntry();

        void showMusicPlaying();

        void showMusicStopped();

        void showToast(@NonNull String text);
    }

    interface Presenter extends BasePresenter {
        void displayAllPostsInRealTime();

        void displayUserProfile();

        void initMusic();

        void onResume();

        void onPause();

        void toggleMusic();

        void downloadPost(@NonNull String imageUrl);

        void handleUploadPostFabClick();

        void handleUploadPostCameraClick();

        void handleUploadPostGalleryClick();

        void handleUploadPostCancelClick();

        void handleMyProfileClick();

        void handleShareClick();

        void handleAboutClick();

        void handleLogoutClick();

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {

        void addPostsListener(@NonNull PostsListener postsListener);

        void removePostsListener();

        void downloadImageToFile(@NonNull String imageUrl, @NonNull DownloadCallback downloadCallback);

        @NonNull
        Uri getSelectedImageFromGallery(@NonNull Intent data)
                throws GalleryResult.SelectedImageNotFoundException;

        interface DownloadCallback {
            void onImageDownloaded(@NonNull File imageFile);

            void onError();
        }
    }
}
