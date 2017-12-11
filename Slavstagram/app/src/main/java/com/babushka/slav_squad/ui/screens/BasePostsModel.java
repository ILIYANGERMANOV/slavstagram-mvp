package com.babushka.slav_squad.ui.screens;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.DatabaseListener;
import com.babushka.slav_squad.persistence.database.model.Post;
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
    protected DatabaseListener<Post> buildIsLikedPostTransformator(@NonNull final DatabaseListener<Post> postsListener) {
        return new DatabaseListener<Post>() {
            @Override
            public void onAdded(@NonNull Post post) {
                setPostLiked(post);
                postsListener.onAdded(post);
            }

            @Override
            public void onChanged(@NonNull Post post) {
                setPostLiked(post);
                postsListener.onChanged(post);
            }

            @Override
            public void onRemoved(@NonNull Post post) {
                postsListener.onRemoved(post);
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {
                postsListener.onError(databaseError);
            }
        };
    }

    private void setPostLiked(@NonNull Post post) {
        Map<String, Boolean> likes = post.getLikes();
        if (likes != null) {
            iterateLikes(post, likes);
        }
    }

    private void iterateLikes(@NonNull Post post, @NonNull Map<String, Boolean> likes) {
        for (String likedUserId : likes.keySet()) {
            if (likedUserId.equals(mUserId)) {
                post.setLiked(true);
                break;
            }
        }
    }
}
