package com.babushka.slav_squad.ui.screens.main.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.MusicPlayer;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.event.DownloadPostEvent;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.special_start.SpecialStart;
import com.babushka.slav_squad.ui.screens.DefaultDisplayPostsListener;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.landing.view.LandingActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by iliyan on 29.05.17.
 */

public class MainPresenter implements MainContract.Presenter {
    public static final String ACTION_POST_PREVIEW = "action_post_preview";
    public static final String POST_ID = "post_id";

    //TODO: Update once we release
    public static final String GOOGLE_PLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.gepardy.gamena";

    private static final int RC_UPLOAD_POST = 1;
    private static final int RC_GALLERY = 2;

    @NonNull
    private final MainContract.Model mModel;
    @NonNull
    private final SessionManager mSessionManager;
    @NonNull
    private final MusicPlayer mMusicPlayer;
    private MainContract.View mView;
    @Nullable
    private DefaultDisplayPostsListener mPostsListener;
    private boolean mIsPlayingMusic;

    public MainPresenter(@NonNull MainContract.View view, @NonNull MainContract.Model model,
                         @NonNull SessionManager sessionManager, @NonNull MusicPlayer musicPlayer) {
        mView = view;
        mModel = model;
        mSessionManager = sessionManager;
        mMusicPlayer = musicPlayer;
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onDownloadPost(DownloadPostEvent event) {
        mView.downloadPostWithPermissionCheck(event.getImageUrl());
    }

    @Override
    public void handleSpecialStart(@Nullable SpecialStart specialStart) {
        if (specialStart != null) {
            switch (specialStart.getAction()) {
                case ACTION_POST_PREVIEW:
                    handlePostPreviewStart(specialStart.getData());
                    break;
            }
        }
    }

    private void handlePostPreviewStart(Map<String, String> data) {
        String postId = data.get(POST_ID);
        mModel.retrievePost(postId, new MainContract.Model.RetrievePostCallback() {
            @Override
            public void onPostRetrieved(@NonNull Post post) {
                if (mView != null) {
                    mView.openPostPreview(post);
                }
            }

            @Override
            public void onError() {
                if (mView != null) {
                    mView.showToast("Post not found :/");
                }
            }
        });
    }

    @Override
    public void displayAllPostsInRealTime() {
        mPostsListener = new DefaultDisplayPostsListener(mView);
        mModel.addPostsListener(mPostsListener);
    }

    @Override
    public void displayUserProfile() {
        User currentUser = mSessionManager.getCurrentUser();
        String displayName = currentUser.getDisplayName();
        if (displayName == null) {
            displayName = currentUser.getEmail();
        }
        mView.displayUserName(displayName);

        String highResPicture = currentUser.getHighResPhotoUrl();
        mView.displayUserProfilePicture(highResPicture);
    }

    @Override
    public void initMusic() {
        mIsPlayingMusic = mMusicPlayer.isVolumeOn() &&
                LandingActivity.sWasLanding;
        mMusicPlayer.volumeOn();
        if (mIsPlayingMusic) {
            playMusic();
        } else {
            pauseMusic();
        }
    }

    @Override
    public void onResume() {
        if (mIsPlayingMusic) {
            playMusic();
        }
    }

    @Override
    public void onPause() {
        pauseMusic();
    }

    @Override
    public void toggleMusic() {
        if (mIsPlayingMusic) {
            pauseMusic();
        } else {
            playMusic();
        }
        mIsPlayingMusic = !mIsPlayingMusic;
    }

    private void pauseMusic() {
        mMusicPlayer.pause();
        mView.showMusicStopped();
    }

    private void playMusic() {
        if (!mMusicPlayer.isLoaded()) {
            mMusicPlayer.loadRepeatable(R.raw.hardbass_mix);
        }
        mMusicPlayer.play();
        mView.showMusicPlaying();
    }

    @Override
    public void downloadPost(@NonNull String imageUrl) {
        mView.showDownloadPostLoading();
        mModel.downloadImageToFile(imageUrl, new MainContract.Model.DownloadCallback() {
            @Override
            public void onImageDownloaded(@NonNull File imageFile) {
                if (mView != null) {
                    mView.showDownloadPostSuccess();
                    mView.addImageToGallery(imageFile);
                }
            }

            @Override
            public void onError() {
                if (mView != null) {
                    mView.showDownloadPostError();
                }
            }
        });
    }

    @Override
    public void handleUploadPostFabClick() {
        boolean isGuest = mSessionManager.getCurrentFirebaseUser().isAnonymous();
        if (isGuest) {
            mView.promptGuestToLogin();
        } else {
            mView.showUploadPostLayout();
        }
    }

    @Override
    public void handleUploadPostCameraClick() {
        mView.uploadPostViaCamera(RC_UPLOAD_POST);
    }

    @Override
    public void handleUploadPostGalleryClick() {
        mView.openGalleryWithCheck(RC_GALLERY);
    }

    @Override
    public void handleUploadPostCancelClick() {
        mView.hideUploadPostLayout();
    }

    @Override
    public void handleMyProfileClick() {
        mView.openProfileScreen();
    }

    @Override
    public void handleShareClick() {
        mView.fireShareIntent("Every true slav has its squad. Join the squad at: " + GOOGLE_PLAY_DOWNLOAD_URL);
    }

    @Override
    public void handleAboutClick() {
        mView.openAboutScreen();
    }

    @Override
    public void handleLogoutClick() {
        SessionManager.getInstance().logout();
        mView.startSplashScreenAsEntry();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_GALLERY:
                handleGalleryResult(resultCode, data);
                break;
            case RC_UPLOAD_POST:
                handleUploadPostResult(resultCode);
                break;
        }
    }

    private void handleGalleryResult(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImage = mModel.getSelectedImageFromGallery(data);
                mView.openUploadPostScreen(RC_UPLOAD_POST, selectedImage);
            } catch (GalleryResult.SelectedImageNotFoundException e) {
                e.printStackTrace();
                mView.showToast("Selected image path not found :(");
            }
        }
    }

    private void handleUploadPostResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            mView.hideUploadPostLayout();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mModel.removePostsListener();
        if (mPostsListener != null) {
            mPostsListener.destroy();
        }
        mView = null;
    }
}
