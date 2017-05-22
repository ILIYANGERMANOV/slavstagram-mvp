package com.babushka.slav_squad.ui.screens.login.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.login.LoginContract;

/**
 * Created by iliyan on 22.05.17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;

    public LoginPresenter(@NonNull LoginContract.View view) {
        mView = view;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
