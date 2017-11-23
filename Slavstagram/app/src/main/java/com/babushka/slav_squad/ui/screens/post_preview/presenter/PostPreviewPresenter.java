package com.babushka.slav_squad.ui.screens.post_preview.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.screens.comments.presenter.CommentsPresenter;
import com.babushka.slav_squad.ui.screens.post_preview.PostPreviewContract;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by iliyan on 26.09.17.
 */

public class PostPreviewPresenter extends CommentsPresenter implements PostPreviewContract.Presenter {
    @NonNull
    private final PostPreviewContract.Model mModel;
    @NonNull
    private final Post mPost;
    private PostPreviewContract.View mView;

    public PostPreviewPresenter(@NonNull PostPreviewContract.View view, @NonNull PostPreviewContract.Model model,
                                @NonNull Post post) {
        super(view, model);
        mView = view;
        mModel = model;
        mPost = post;
    }

    @Override
    public void setupUI() {
        super.setupUI();
        mView.displayPostImage(mPost.getImage());
        mView.displayPostLikesCount(mPost.getLikesCount());
        Map<String, User> likes = mPost.getLikes();
        if (likes != null) {
            mView.displayPostLikes(new ArrayList<>(likes.values()));
        }
        mView.displayDescription(mPost.getDescription());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
    }
}
