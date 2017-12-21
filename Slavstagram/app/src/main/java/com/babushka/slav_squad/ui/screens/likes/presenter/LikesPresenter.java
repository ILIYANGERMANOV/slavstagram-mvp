package com.babushka.slav_squad.ui.screens.likes.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.ValueListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;
import com.babushka.slav_squad.ui.screens.likes.view.DisplayLikesRealtimeListener;
import com.google.firebase.database.DatabaseError;

/**
 * Created by iliyan on 09.11.17.
 */

public class LikesPresenter implements LikesContract.Presenter {
    @NonNull
    private final Post mPost;
    @NonNull
    private final LikesContract.Model mModel;
    private LikesContract.View mView;
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
        mModel.addLikesCountListener(mPost.getId(), new ValueListener<Integer>() {
            @Override
            public void onChanged(Integer likesCount) {
                if (mView != null) {
                    mView.displayLikesCount(likesCount);
                }
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {
                if (mView != null) {
                    mView.showToast("Database error: " + databaseError.getMessage());
                }
            }
        });
        mView.displayLikesCount(mPost.getLikesCount());
    }

    @Override
    public void onDestroy() {
        mModel.removeLikesListener(mPost.getId());
        mModel.removeLikesCountListener(mPost.getId());
        mLikesRealtimeListener.destroy();
        mView = null;
    }
}
