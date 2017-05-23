package com.babushka.slav_squad.ui.screens.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.session.FacebookLoginAdapter;
import com.babushka.slav_squad.session.FacebookLoginCallback;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.session.UserDetails;
import com.babushka.slav_squad.ui.screens.login.LoginContract;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 22.05.17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    @NonNull
    private final SessionManager mSessionManager;
    private LoginContract.View mView;
    @Nullable
    private FacebookLoginAdapter mFacebookLoginAdapter;

    public LoginPresenter(@NonNull LoginContract.View view) {
        mView = view;
        mSessionManager = SessionManager.getInstance();
    }

    @Override
    public void setupFacebookLogin(@NonNull Activity activity, @NonNull LoginButton fbLoginButton) {
        mFacebookLoginAdapter = mSessionManager.loginWithFacebook(activity, fbLoginButton,
                new FacebookLoginCallback() {
                    @Override
                    public void onSuccess(@NonNull FirebaseUser user) {
                        mView.restartApp();
                    }

                    @Override
                    public void onCancel() {
                        if (mView != null) {
                            mView.showToast("Facebook login canceled");
                        }
                    }

                    @Override
                    public void onError(@Nullable Exception exception) {
                        if (mView != null) {
                            String message = "unknown error";
                            if (exception != null) {
                                message = exception.getMessage();
                            }
                            mView.showToast("Error while login with FB: " + message);
                        }
                    }
                });
    }

    @Override
    public void loginWithGooglePlus() {

    }


    @Override
    public void loginWithUsernameAndPassword(@NonNull UserDetails userDetails) {

    }

    @Override
    public void handleRegisterClick() {

    }

    @Override
    public void loginAsGuest() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFacebookLoginAdapter != null) {
            mFacebookLoginAdapter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
