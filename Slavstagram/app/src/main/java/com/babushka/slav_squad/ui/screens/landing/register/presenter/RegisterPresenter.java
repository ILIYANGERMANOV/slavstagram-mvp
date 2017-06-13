package com.babushka.slav_squad.ui.screens.landing.register.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.landing.register.RegisterContract;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterPresenter implements RegisterContract.Presenter {
    @NonNull
    private final RegisterContract.Model mModel;
    private RegisterContract.View mView;

    public RegisterPresenter(@NonNull RegisterContract.View view, @NonNull RegisterContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
