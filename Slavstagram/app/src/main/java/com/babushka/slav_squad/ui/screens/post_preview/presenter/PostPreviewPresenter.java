package com.babushka.slav_squad.ui.screens.post_preview.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.comments.presenter.CommentsPresenter;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;
import com.babushka.slav_squad.ui.screens.likes.view.DisplayLikesRealtimeListener;
import com.babushka.slav_squad.ui.screens.post_preview.PostPreviewContract;

/**
 * Created by iliyan on 26.09.17.
 */

public class PostPreviewPresenter extends CommentsPresenter implements PostPreviewContract.Presenter {
    @NonNull
    private final PostPreviewContract.Model mModel;
    @NonNull
    private final LikesContract.Model mLikesModel;
    @NonNull
    private final Post mPost;
    private PostPreviewContract.View mView;
    private LikesContract.View mLikesView;
    private DisplayLikesRealtimeListener mLikesRealtimeListener;

    public PostPreviewPresenter(@NonNull PostPreviewContract.View view, @NonNull PostPreviewContract.Model model,
                                @NonNull LikesContract.Model likesModel, @NonNull LikesContract.View likesView,
                                @NonNull Post post) {
        super(view, model);
        mView = view;
        mModel = model;
        mLikesModel = likesModel;
        mLikesView = likesView;
        mPost = post;
    }

    @Override
    public void setupUI() {
        super.setupUI();
        mView.displayPostImage(mPost.getImage());
        displayLikes();
        mView.displayDescription(mPost.getDescription());
    }

    private void displayLikes() {
        mLikesRealtimeListener = new DisplayLikesRealtimeListener(mLikesView);
        mLikesModel.addLikesListener(mPost.getId(), mLikesRealtimeListener);
        //TODO: Display likes count in realtime
        mView.displayPostLikesCount(mPost.getLikesCount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLikesModel.removeLikesListener(mPost.getId());
        mLikesRealtimeListener.destroy();
        mView = null;
        mLikesView = null;
    }
}
