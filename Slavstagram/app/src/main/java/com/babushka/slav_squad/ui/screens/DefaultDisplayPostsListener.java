package com.babushka.slav_squad.ui.screens;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.listeners.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.google.firebase.database.DatabaseError;

/**
 * Created by iliyan on 07.06.17.
 */

public class DefaultDisplayPostsListener implements PostsListener {
    @Nullable
    private PostsView mView;

    public DefaultDisplayPostsListener(@NonNull PostsView view) {
        mView = view;
    }

    @Override
    public void onPostAdded(@NonNull Post post) {
        if (mView != null) {
            mView.addPostAsFirst(post);
        }
    }

    @Override
    public void onPostChanged(@NonNull Post post) {
        if (mView != null) {
            mView.updatePost(post);
        }
    }

    @Override
    public void onPostRemoved(@NonNull Post post) {
        if (mView != null) {
            mView.removePost(post);
        }

    }

    @Override
    public void onError(@NonNull DatabaseError databaseError) {
        //TODO: Implement method
    }

    public void destroy() {
        mView = null;
    }
}
