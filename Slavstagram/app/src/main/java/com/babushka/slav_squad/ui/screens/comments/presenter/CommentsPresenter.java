package com.babushka.slav_squad.ui.screens.comments.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.comments.CommentsContract;

/**
 * Created by iliyan on 10.06.17.
 */

public class CommentsPresenter implements CommentsContract.Presenter {
    @NonNull
    private final CommentsContract.Model mModel;
    private CommentsContract.View mView;

    public CommentsPresenter(@NonNull CommentsContract.View view, @NonNull CommentsContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
