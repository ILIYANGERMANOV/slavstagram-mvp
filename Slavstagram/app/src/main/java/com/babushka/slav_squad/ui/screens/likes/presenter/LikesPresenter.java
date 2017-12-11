package com.babushka.slav_squad.ui.screens.likes.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;
import com.babushka.slav_squad.ui.screens.likes.view.DisplayLikesRealtimeListener;

/**
 * Created by iliyan on 09.11.17.
 */

public class LikesPresenter implements LikesContract.Presenter {
    @NonNull
    private final Post mPost;
    private LikesContract.View mView;
    @NonNull
    private final LikesContract.Model mModel;
    private DisplayLikesRealtimeListener mLikesRealtimeListener;

    public LikesPresenter(@NonNull LikesContract.View view, @NonNull Post post,
                          @NonNull LikesContract.Model model) {
        mView = view;
        mPost = post;
        mModel = model;
    }

    @Override
    public void displayLikes() {
        mLikesRealtimeListener = new DisplayLikesRealtimeListener(mView);
        mModel.addLikesListener(mPost.getId(), mLikesRealtimeListener);
    }

    @Override
    public void onDestroy() {
        mModel.removeLikesListener(mPost.getId());
        mLikesRealtimeListener.destroy();
        mView = null;
    }
}
