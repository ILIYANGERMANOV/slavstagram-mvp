package com.babushka.slav_squad.ui.screens.preview_post.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.comments.presenter.CommentsPresenter;
import com.babushka.slav_squad.ui.screens.preview_post.PreviewPostContract;

/**
 * Created by iliyan on 26.09.17.
 */

public class PreviewPostPresenter extends CommentsPresenter implements PreviewPostContract.Presenter {
    @NonNull
    private final PreviewPostContract.Model mModel;
    private PreviewPostContract.View mView;

    public PreviewPostPresenter(@NonNull PreviewPostContract.View view, @NonNull PreviewPostContract.Model model) {
        super(view, model);
        mView = view;
        mModel = model;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
    }
}
