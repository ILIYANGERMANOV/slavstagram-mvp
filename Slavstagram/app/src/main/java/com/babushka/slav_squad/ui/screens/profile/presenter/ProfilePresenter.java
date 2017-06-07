package com.babushka.slav_squad.ui.screens.profile.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.screens.DefaultDisplayPostsListener;
import com.babushka.slav_squad.ui.screens.profile.ProfileContract;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfilePresenter implements ProfileContract.Presenter {
    @NonNull
    private final ProfileContract.Model mModel;
    @NonNull
    private final User mUser;
    private ProfileContract.View mView;
    @Nullable
    private DefaultDisplayPostsListener mPostsListener;

    public ProfilePresenter(@NonNull ProfileContract.View view, @NonNull ProfileContract.Model model,
                            @NonNull User user) {
        mView = view;
        mModel = model;
        mUser = user;
    }

    @Override
    public void displayUserAndLoadHisPosts() {
        displayUser();
        displayUserPosts();
    }

    private void displayUserPosts() {
        mPostsListener = new DefaultDisplayPostsListener(mView);
        mModel.addUserPostsListener(mPostsListener);
    }

    private void displayUser() {
        String photoUrl = mUser.getPhotoUrl();
        String displayName = mUser.getDisplayName();
        if (photoUrl != null && displayName != null) {
            mView.displayUser(photoUrl, displayName);
        }
    }

    @Override
    public void onDestroy() {
        mModel.removeUserPostsListener();
        if (mPostsListener != null) {
            mPostsListener.destroy();
        }
        mView = null;
    }
}
