package com.babushka.slav_squad.session;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

/**
 * Created by iliyan on 23.05.17.
 */

public class FacebookLoginAdapter {
    public static final String[] PERMISSIONS = {"email", "public_profile"};
    private static final String TAG = "FacebookLoginAdapter";
    @NonNull
    private final CallbackManager mCallbackManager;
    @NonNull
    private final LoginButton mLoginButton;

    FacebookLoginAdapter(@NonNull LoginButton loginButton) {
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton = loginButton;
    }

    public void setupLoginButton(@NonNull final Callback callback) {
        mLoginButton.setReadPermissions(PERMISSIONS);
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                AccessToken token = loginResult.getAccessToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                callback.onSuccess(credential);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                callback.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                callback.onError(error);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    interface Callback {
        void onSuccess(@NonNull AuthCredential credential);

        void onCancel();

        void onError(@NonNull FacebookException error);
    }
}
