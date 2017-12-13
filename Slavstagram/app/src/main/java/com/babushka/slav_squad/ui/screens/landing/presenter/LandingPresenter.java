package com.babushka.slav_squad.ui.screens.landing.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.babushka.slav_squad.MusicPlayer;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.SimpleAnalytics;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.session.FacebookLoginCallback;
import com.babushka.slav_squad.session.FirebaseLoginCallback;
import com.babushka.slav_squad.session.LoginAdapter;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.AnalyticsPresenter;
import com.babushka.slav_squad.ui.screens.landing.LandingContract;
import com.babushka.slav_squad.ui.screens.landing.LandingModel;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 22.05.17.
 */

public class LandingPresenter extends AnalyticsPresenter implements LandingContract.Presenter {
    @NonNull
    private final SessionManager mSessionManager;
    @NonNull
    private final LandingModel mModel;
    private final MusicPlayer mPlayer;
    private LandingContract.View mView;
    @Nullable
    private LoginAdapter mLoginAdapter;


    public LandingPresenter(@NonNull LandingContract.View view, @NonNull LandingModel model,
                            @NonNull MusicPlayer musicPlayer, @NonNull SimpleAnalytics analytics) {
        super(analytics);
        mView = view;
        mModel = model;
        mSessionManager = SessionManager.getInstance();
        mPlayer = musicPlayer;
        musicPlayer.loadRepeatable(R.raw.landing_mix);
    }

    @Override
    public void setupFacebookLogin(@NonNull Activity activity, @NonNull LoginButton fbLoginButton) {
        //!NOTE: progress dialog is showed internal by the view
        mLoginAdapter = mSessionManager.loginWithFacebook(activity, fbLoginButton,
                new FacebookLoginCallback() {
                    @Override
                    public void onSuccess(@NonNull FirebaseUser user) {
                        mAnalytics.logEvent(Events.Landing.LOGIN_WITH_FB_SUCCESS);
                        saveUserAndHandleSuccessfulLogin(user);
                    }

                    @Override
                    public void onCancel() {
                        mAnalytics.logEvent(Events.Landing.LOGIN_WITH_FB_CANCEL);
                        if (mView != null) {
                            mView.hideProgress();
                            mView.showToast("Facebook signIn canceled");
                        }
                    }

                    @Override
                    public void onError(@Nullable Exception exception) {
                        mAnalytics.logEvent(Events.Landing.LOGIN_WITH_FB_ERROR);
                        handleLoginError("Error while signIn with FB: ", exception);
                    }
                });
    }

    @Override
    public void loginWithGoogle(@NonNull AppCompatActivity activity) {
        mView.showProgress();
        mAnalytics.logEvent(Events.Landing.LOGIN_WITH_GOOGLE);
        mLoginAdapter = mSessionManager.loginWithGoogle(activity, new FirebaseLoginCallback() {
            @Override
            public void onSuccess(@NonNull FirebaseUser user) {
                mAnalytics.logEvent(Events.Landing.LOGIN_WITH_GOOGLE_SUCCESS);
                saveUserAndHandleSuccessfulLogin(user);
            }

            @Override
            public void onError(@Nullable Exception exception) {
                mAnalytics.logEvent(Events.Landing.LOGIN_WITH_GOOGLE_ERROR);
                handleLoginError("Error while signIn with Google: ", exception);
            }
        });
    }

    @Override
    public void handleEmailClick() {
        mAnalytics.logEvent(Events.Landing.EMAIL_CLICK);
        mView.startLoginScreen();
    }

    @Override
    public void loginAsGuest() {
        mView.showProgress();
        mSessionManager.loginAsGuest(new FirebaseLoginCallback() {
            @Override
            public void onSuccess(@NonNull FirebaseUser user) {
                handleSuccessfulLogin();
            }

            @Override
            public void onError(@Nullable Exception exception) {
                handleLoginError("Error while login as guest: ", exception);
            }
        });
    }

    private void saveUserAndHandleSuccessfulLogin(@NonNull FirebaseUser user) {
        mModel.saveUser(user);
        handleSuccessfulLogin();
    }

    private void handleSuccessfulLogin() {
        if (mView != null) {
            mView.hideProgress();
            mView.restartApp();
        }
    }

    private void handleLoginError(@NonNull String localizedMessage, @Nullable Exception exception) {
        if (mView != null) {
            mView.hideProgress();
            String message = "unknown error";
            if (exception != null) {
                message = exception.getMessage();
            }
            mView.showToast(localizedMessage + message);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mLoginAdapter != null) {
            mLoginAdapter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void playMusic() {
        mPlayer.play();
    }

    @Override
    public void pauseMusic() {
        mPlayer.pause();
    }

    @Override
    public void onDestroy() {
        mView.hideProgress();
        mView = null;
        mLoginAdapter = null;
    }
}
