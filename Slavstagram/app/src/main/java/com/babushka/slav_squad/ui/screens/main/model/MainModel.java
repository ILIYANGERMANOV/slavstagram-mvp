package com.babushka.slav_squad.ui.screens.main.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

/**
 * Created by iliyan on 02.06.17.
 */

public class MainModel implements MainContract.Model {
    @NonNull
    private final String mUserId;

    public MainModel(@NonNull FirebaseUser user) {
        mUserId = user.getUid();
    }

    @Override
    public void addPostsListener(@NonNull final PostsListener postsListener) {
        Database.getInstance().addPostsListener(new PostsListener() {
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
        });
    }

    private void setPostLiked(@NonNull Post post) {
        Map<String, Boolean> likes = post.getLikes();
        if (likes != null) {
            for (String likedUserId : likes.keySet()) {
                if (likedUserId.equals(mUserId)) {
                    post.setLiked(true);
                    break;
                }
            }
        }
    }

    @Override
    public void removePostsListener() {
        Database.getInstance().removePostsListener();
    }
}
