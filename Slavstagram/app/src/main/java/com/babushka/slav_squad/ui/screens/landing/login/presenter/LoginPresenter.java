package com.babushka.slav_squad.ui.screens.landing.login.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.babushka.slav_squad.session.FirebaseLoginCallback;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.session.UserDetails;
import com.babushka.slav_squad.ui.screens.landing.LandingModel;
import com.babushka.slav_squad.ui.screens.landing.login.LoginContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    @NonNull
    private final LandingModel mModel;
    private LoginContract.View mView;

    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LandingModel model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void handleInput(@NonNull UserDetails userDetails) {
        if (isValidInput(userDetails)) {
            mView.enableLoginButton();
        } else {
            mView.disableLoginButton();
        }
    }

    @Override
    public boolean isValidInput(@NonNull UserDetails userDetails) {
        return isValidEmail(userDetails.getEmail()) &&
                !TextUtils.isEmpty(userDetails.getPassword());
    }

    private boolean isValidEmail(@NonNull String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void login(@NonNull UserDetails userDetails) {
        SessionManager.getInstance().loginWithEmailAndPassword(userDetails, new FirebaseLoginCallback() {
            @Override
            public void onSuccess(@NonNull FirebaseUser user) {
                mModel.saveUser(user);
                if (mView != null) {
                    mView.restartApp();
                }
            }

            @Override
            public void onError(@Nullable Exception exception) {
                if (exception != null && mView != null) {
                    mView.showToast(exception.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
