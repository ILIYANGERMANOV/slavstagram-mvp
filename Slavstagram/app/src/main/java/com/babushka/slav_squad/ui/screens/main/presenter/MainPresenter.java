package com.babushka.slav_squad.ui.screens.main.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.MusicPlayer;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.event.DownloadPostEvent;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.DefaultDisplayPostsListener;
import com.babushka.slav_squad.ui.screens.main.MainContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * Created by iliyan on 29.05.17.
 */

public class MainPresenter implements MainContract.Presenter {
    public static final String GOOGLE_PLAY_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.gepardy.gamena";
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

        String photoUrl = currentUser.getPhotoUrl();
        if (photoUrl != null) {
            mView.displayUserProfilePicture(photoUrl);
        }
    }

    @Override
    public void initMusic() {
        mIsPlayingMusic = mMusicPlayer.isVolumeOn();
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
    public void handleUploadPostClick() {
        boolean isGuest = mSessionManager.getCurrentFirebaseUser().isAnonymous();
        if (isGuest) {
            mView.promptGuestToLogin();
        } else {
            mView.openUploadPostScreen();
        }
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
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mModel.removePostsListener();
        if (mPostsListener != null) {
            mPostsListener.destroy();
        }
        mView = null;
    }
}
