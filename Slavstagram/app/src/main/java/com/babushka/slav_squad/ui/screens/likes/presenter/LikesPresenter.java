package com.babushka.slav_squad.ui.screens.likes.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by iliyan on 09.11.17.
 */

public class LikesPresenter implements LikesContract.Presenter {
    @NonNull
    private final Post mPost;
    private LikesContract.View mView;

    public LikesPresenter(@NonNull LikesContract.View view, @NonNull Post post) {
        mView = view;
        mPost = post;
    }

    @Override
    public void displayLikes() {
        Map<String, User> likes = mPost.getLikes();
        if (likes != null) {
            mView.displayLikes(new ArrayList<>(likes.values()));
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
