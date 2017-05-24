package com.babushka.slav_squad.ui.screens.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.babushka.slav_squad.session.UserDetails;
import com.babushka.slav_squad.ui.screens.BasePresenter;
import com.babushka.slav_squad.ui.screens.BaseView;
import com.facebook.login.widget.LoginButton;

/**
 * Created by iliyan on 22.05.17.
 */

public interface LoginContract {
    interface View extends BaseView {
        void startRegisterScreen();

        void restartApp();

        void showToast(@NonNull String message);
    }

    interface Presenter extends BasePresenter {
        void setupFacebookLogin(@NonNull Activity activity, @NonNull LoginButton fbLoginButton);

        void loginWithGoogle(@NonNull AppCompatActivity activity);

        void loginWithEmailAndPassword(@NonNull Activity activity, @NonNull UserDetails userDetails);

        void handleRegisterClick();

        void loginAsGuest(@NonNull Activity activity);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
