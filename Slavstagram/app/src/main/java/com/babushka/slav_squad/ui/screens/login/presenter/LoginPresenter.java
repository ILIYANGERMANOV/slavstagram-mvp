package com.babushka.slav_squad.ui.screens.login.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.analytics.SimpleAnalytics;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.session.FirebaseLoginCallback;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.ui.AnalyticsPresenter;
import com.babushka.slav_squad.ui.screens.landing.LandingModel;
import com.babushka.slav_squad.ui.screens.login.LoginContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public class LoginPresenter extends AnalyticsPresenter implements LoginContract.Presenter {
    @NonNull
    private final LandingModel mModel;
    private LoginContract.View mView;

    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LandingModel model,
                          @NonNull SimpleAnalytics analytics) {
        super(analytics);
        mView = view;
        mModel = model;
    }

    @Override
    public void handleInput(@NonNull LoginDetails loginDetails) {
        if (isValidInput(loginDetails)) {
            mView.enableLoginButton();
        } else {
            mView.disableLoginButton();
        }
    }

    @Override
    public boolean isValidInput(@NonNull LoginDetails loginDetails) {
        return loginDetails.validate();
    }

    @Override
    public void login(@NonNull LoginDetails loginDetails) {
        mView.showProgress();
        mAnalytics.logEvent(Events.Login.LOGIN_WITH_EMAIL);
        SessionManager.getInstance().loginWithEmailAndPassword(loginDetails, new FirebaseLoginCallback() {
            @Override
            public void onSuccess(@NonNull FirebaseUser user) {
                mAnalytics.logEvent(Events.Login.LOGIN_WITH_EMAIL_SUCCESS);
                mModel.saveUser(user);
                if (mView != null) {
                    mView.hideProgress();
                    mView.restartApp();
                }
            }

            @Override
            public void onError(@Nullable Exception exception) {
                mAnalytics.logEvent(Events.Login.LOGIN_WITH_EMAIL_ERROR);
                if (mView != null)
                    mView.hideProgress();
                if (exception != null) {
                    mView.showToast(exception.getMessage());
                }
            }
        });
    }

    @Override
    public void handleRegisterButtonClick() {
        mAnalytics.logEvent(Events.Login.LOGIN_REGISTER_CLICK);
        mView.startRegisterScreen();
    }

    @Override
    public void onDestroy() {
        mView.hideProgress();
        mView = null;
    }
}
