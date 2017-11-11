package com.babushka.slav_squad.ui.screens.login;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.screens.landing.LandingView;

/**
 * Created by iliyan on 11.06.17.
 */

public interface LoginContract {
    interface View extends LandingView {
        void enableLoginButton();

        void disableLoginButton();

        void startRegisterScreen();

        void showToast(@NonNull String message);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends BasePresenter {
        void handleInput(@NonNull LoginDetails loginDetails);

        boolean isValidInput(@NonNull LoginDetails loginDetails);

        void login(@NonNull LoginDetails loginDetails);

        void handleRegisterButtonClick();
    }
}
