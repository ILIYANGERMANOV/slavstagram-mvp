package com.babushka.slav_squad.ui.screens;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

/**
 * Created by iliyan on 07.06.17.
 */

public class BasePostsModel {
    @NonNull
    protected final String mUserId;

    public BasePostsModel(@NonNull String userId) {
        mUserId = userId;
    }

    @NonNull
    protected PostsListener buildIsLikedPostTransformator(@NonNull final PostsListener postsListener) {
        return new PostsListener() {
            @Override
            public void onPostAdded(@NonNull Post post) {
                setPostLiked(post);
                postsListener.onPostAdded(post);
            }

            @Override
            public void onPostChanged(@NonNull Post post) {
                setPostLiked(post);
                postsListener.onPostChanged(post);
            }

            @Override
            public void onPostRemoved(@NonNull Post post) {
                postsListener.onPostRemoved(post);
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {
                postsListener.onError(databaseError);
            }
        };
    }

    private void setPostLiked(@NonNull Post post) {
        Map<String, User> likes = post.getLikes();
        if (likes != null) {
            iterateLikes(post, likes);
        }
    }

    private void iterateLikes(@NonNull Post post, @NonNull Map<String, User> likes) {
        for (String likedUserId : likes.keySet()) {
            if (likedUserId.equals(mUserId)) {
                post.setLiked(true);
                break;
            }
        }
    }
}
