package com.babushka.slav_squad.ui.screens.profile.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.profile.ProfileContract;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfilePresenter implements ProfileContract.Presenter {
    @NonNull
    private final ProfileContract.Model mModel;
    private ProfileContract.View mView;

    public ProfilePresenter(@NonNull ProfileContract.View view,
                            @NonNull ProfileContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
