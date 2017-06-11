package com.babushka.slav_squad.ui.screens.comments.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.CommentsListener;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.comments.CommentsContract;
import com.google.firebase.database.DatabaseError;

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
    public void displayCurrentUser() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        mView.displayAuthorImage(currentUser.getPhotoUrl());
    }

    @Override
    public void displayCommentsInRealTime() {
        mModel.addCommentsListener(new CommentsListener() {
            @Override
            public void onCommentAdded(@NonNull Comment comment) {
                if (mView != null) {
                    mView.addComment(comment);
                }
            }

            @Override
            public void onCommentChanged(@NonNull Comment comment) {
                if (mView != null) {
                    mView.updateComment(comment);
                }
            }

            @Override
            public void onCommentRemoved(@NonNull Comment comment) {
                if (mView != null) {
                    mView.removeComment(comment);
                }
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {
                //TODO: Implement method
            }
        });
    }

    @Override
    public void addComment(@NonNull Comment comment) {
        mModel.addComment(comment);
    }

    @Override
    public void onDestroy() {
        mModel.removeCommentsListener();
        mView = null;
    }
}
