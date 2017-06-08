package com.babushka.slav_squad.ui.screens.login.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.babushka.slav_squad.session.FacebookLoginCallback;
import com.babushka.slav_squad.session.FirebaseLoginCallback;
import com.babushka.slav_squad.session.LoginAdapter;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.session.UserDetails;
import com.babushka.slav_squad.ui.screens.login.LoginContract;
import com.babushka.slav_squad.ui.screens.login.model.LoginModel;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 22.05.17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    @NonNull
    private final SessionManager mSessionManager;
    @NonNull
    private final LoginModel mLoginModel;
    private LoginContract.View mView;
    @Nullable
    private LoginAdapter mLoginAdapter;


    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LoginModel loginModel) {
        mView = view;
        mLoginModel = loginModel;
        mSessionManager = SessionManager.getInstance();
    }

    @Override
    public void setupFacebookLogin(@NonNull Activity activity, @NonNull LoginButton fbLoginButton) {
        mLoginAdapter = mSessionManager.loginWithFacebook(activity, fbLoginButton,
                new FacebookLoginCallback() {
                    @Override
                    public void onSuccess(@NonNull FirebaseUser user) {
                        saveUserAndHandleSuccessfulLogin(user);
                    }

                    @Override
                    public void onCancel() {
                        if (mView != null) {
                            mView.showToast("Facebook signIn canceled");
                        }
                    }

                    @Override
                    public void onError(@Nullable Exception exception) {
                        handleLoginError("Error while signIn with FB: ", exception);
                    }
                });
    }

    @Override
    public void loginWithGoogle(@NonNull AppCompatActivity activity) {
        mLoginAdapter = mSessionManager.loginWithGoogle(activity, new FirebaseLoginCallback() {
            @Override
            public void onSuccess(@NonNull FirebaseUser user) {
                saveUserAndHandleSuccessfulLogin(user);
            }

            @Override
            public void onError(@Nullable Exception exception) {
                handleLoginError("Error while signIn with Google: ", exception);
            }
        });
    }

    @Override
    public void loginWithEmailAndPassword(@NonNull Activity activity, @NonNull UserDetails userDetails) {
        if (userDetails.validate()) {
            loginWithValidatedUserDetails(activity, userDetails);
        } else {
            mView.showToast("Email and password cannot be empty fields!");
        }
    }

    private void loginWithValidatedUserDetails(@NonNull Activity activity, @NonNull UserDetails userDetails) {
        mSessionManager.loginWithEmailAndPassword(activity, userDetails, new FirebaseLoginCallback() {
            @Override
            public void onSuccess(@NonNull FirebaseUser user) {
                saveUserAndHandleSuccessfulLogin(user);
            }

            @Override
            public void onError(@Nullable Exception exception) {
                handleLoginError("Login error: ", exception);
            }
        });
    }

    @Override
    public void handleRegisterClick() {
        //TODO: Implement method
    }

    @Override
    public void loginAsGuest(@NonNull Activity activity) {
        mSessionManager.loginAsGuest(activity, new FirebaseLoginCallback() {
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
        mLoginModel.saveUser(user);
        handleSuccessfulLogin();
    }

    private void handleSuccessfulLogin() {
        if (mView != null) {
            mView.restartApp();
        }
    }

    private void handleLoginError(@NonNull String localizedMessage, @Nullable Exception exception) {
        if (mView != null) {
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
    public void onDestroy() {
        mView = null;
        mLoginAdapter = null;
    }
}
