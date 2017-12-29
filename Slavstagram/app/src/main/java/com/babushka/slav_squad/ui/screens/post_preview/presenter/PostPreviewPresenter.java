package com.babushka.slav_squad.ui.screens.post_preview.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.analytics.SimpleAnalytics;
import com.babushka.slav_squad.analytics.event.EventBuilder;
import com.babushka.slav_squad.analytics.event.EventParamKeys;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.listeners.ValueListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.comments.presenter.CommentsPresenter;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;
import com.babushka.slav_squad.ui.screens.likes.view.DisplayLikesRealtimeListener;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.post_preview.PostPreviewContract;
import com.google.firebase.database.DatabaseError;

import java.io.File;

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
    @NonNull
    private final SimpleAnalytics mAnalytics;
    @NonNull
    private final MainContract.Model mMainModel;
    private PostPreviewContract.View mView;
    private LikesContract.View mLikesView;
    private DisplayLikesRealtimeListener mLikesRealtimeListener;

    public PostPreviewPresenter(@NonNull PostPreviewContract.View view, @NonNull PostPreviewContract.Model model,
                                @NonNull LikesContract.Model likesModel, @NonNull LikesContract.View likesView,
                                @NonNull Post post, @NonNull SimpleAnalytics analytics, @NonNull MainContract.Model mainModel) {
        super(view, model);
        mView = view;
        mModel = model;
        mLikesModel = likesModel;
        mLikesView = likesView;
        mAnalytics = analytics;
        mMainModel = mainModel;
        mPost = post;
    }

    @Override
    public void setupUI() {
        super.setupUI();
        mView.displayPostImage(mPost.getImage());
        displayLikes();
        mView.displayDescription(mPost.getDescription());
        mView.setPostLiked(mPost.isLiked());
    }

    private void displayLikes() {
        mLikesRealtimeListener = new DisplayLikesRealtimeListener(mLikesView);
        mLikesModel.addLikesListener(mPost.getId(), mLikesRealtimeListener);
        mLikesModel.addLikesCountListener(mPost.getId(), new ValueListener<Integer>() {
            @Override
            public void onChanged(Integer likesCount) {
                if (mView != null) {
                    mLikesView.displayLikesCount(likesCount);
                }
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {
                if (mView != null) {
                    mView.showToast("Database error: " + databaseError.getMessage());
                }
            }
        });
        mLikesView.displayLikesCount(mPost.getLikesCount());
    }

    @Override
    public void toggleLike() {
        if (mPost.isLiked()) {
            unlikePost(mPost);
        } else {
            likePost(mPost);
        }
    }

    @Override
    public void downloadPost() {
        mView.showDownloadPostLoading();
        mMainModel.downloadImageToFile(mPost.getImage().getImageUrl(), new MainContract.Model.DownloadCallback() {
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
    public void handlePostLikesCountClick() {
        //Post object may not be present, retrieve it
        mMainModel.retrievePost(mPost.getId(), new MainContract.Model.RetrievePostCallback() {
            @Override
            public void onPostRetrieved(@NonNull Post post) {
                if (mView != null) {
                    mView.openLikesScreen(post);
                }
            }

            @Override
            public void onError() {
                if (mView != null) {
                    mView.showToast("Error: Post not longer accessible");
                }
            }
        });
    }

    private void likePost(@NonNull Post post) {
        if (!post.isLiked()) {
            logPostEvent(Events.Main.POST_LIKE);
            setPostLikedState(post, true);
        }
    }

    private void unlikePost(@NonNull Post post) {
        if (post.isLiked()) {
            logPostEvent(Events.Main.POST_UNLIKE);
            setPostLikedState(post, false);
        }
    }

    private void logPostEvent(@NonNull String eventName) {
        mAnalytics.logEvent(new EventBuilder()
                .setEventName(eventName)
                .addParam(EventParamKeys.POST_ID, mPost.getId())
                .addParam(EventParamKeys.FROM_SCREEN, EventValues.Screen.POST_PREVIEW)
                .build());
    }

    private void setPostLikedState(@NonNull Post post, boolean liked) {
        post.setLiked(liked);
        User user = SessionManager.getInstance().getCurrentUser();
        Database.getInstance().toggleLike(post, user);
        mView.setPostLiked(liked);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLikesModel.removeLikesListener(mPost.getId());
        mLikesModel.removeLikesCountListener(mPost.getId());
        mLikesRealtimeListener.destroy();
        mView = null;
        mLikesView = null;
    }
}
