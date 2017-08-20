package com.babushka.slav_squad.ui.screens.main.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.event.DownloadPostEvent;
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
    private MainContract.View mView;
    @Nullable
    private DefaultDisplayPostsListener mPostsListener;

    public MainPresenter(@NonNull MainContract.View view, @NonNull MainContract.Model model) {
        mView = view;
        mModel = model;
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
    public void handleMyProfileClick() {
        mView.openProfileScreen();
    }

    @Override
    public void handleShareClick() {
        mView.fireShareIntent("Every true slav has its squad. Join the squad at: " + GOOGLE_PLAY_DOWNLOAD_URL);
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
