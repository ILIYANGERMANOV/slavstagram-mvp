package com.babushka.slav_squad.ui.screens.landing;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.babushka.slav_squad.ui.BasePresenter;
import com.facebook.login.widget.LoginButton;

/**
 * Created by iliyan on 22.05.17.
 */

public interface LandingContract {
    interface View extends LandingView {
        void startLoginScreen();

        void showToast(@NonNull String message);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends BasePresenter {

        void setupFacebookLogin(@NonNull Activity activity, @NonNull LoginButton fbLoginButton);

        void loginWithGoogle(@NonNull AppCompatActivity activity);

        void handleEmailClick();

        void loginAsGuest();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void playMusic();

        void pauseMusic();
    }
}
