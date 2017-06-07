package com.babushka.slav_squad.ui.screens.main.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.ui.screens.DefaultDisplayPostsListener;
import com.babushka.slav_squad.ui.screens.main.MainContract;

/**
 * Created by iliyan on 29.05.17.
 */

public class MainPresenter implements MainContract.Presenter {
    @NonNull
    private final MainContract.Model mModel;
    private MainContract.View mView;
    @Nullable
    private DefaultDisplayPostsListener mPostsListener;

    public MainPresenter(@NonNull MainContract.View view, @NonNull MainContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void displayAllPostsInRealTime() {
        mPostsListener = new DefaultDisplayPostsListener(mView);
        mModel.addPostsListener(mPostsListener);
    }

    @Override
    public void onDestroy() {
        mModel.removePostsListener();
        if (mPostsListener != null) {
            mPostsListener.destroy();
        }
        mView = null;
    }
}
