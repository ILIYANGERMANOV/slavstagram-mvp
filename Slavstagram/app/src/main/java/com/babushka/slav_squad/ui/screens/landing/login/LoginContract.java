package com.babushka.slav_squad.ui.screens.landing.login;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.session.UserDetails;
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
    }

    interface Presenter extends BasePresenter {
        void handleInput(@NonNull UserDetails userDetails);

        boolean isValidInput(@NonNull UserDetails userDetails);

        void login(@NonNull UserDetails userDetails);

        void handleRegisterButtonClick();
    }
}
