package com.babushka.slav_squad.ui.screens.main.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.google.firebase.database.DatabaseError;

/**
 * Created by iliyan on 29.05.17.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    @NonNull
    private final MainContract.Model mModel;

    public MainPresenter(@NonNull MainContract.View view, @NonNull MainContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void displayAllPostsInRealtime() {
        mModel.addPostsListener(new PostsListener() {
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
        });
    }

    @Override
    public void onDestroy() {
        mModel.removePostsListener();
        mView = null;
    }
}
