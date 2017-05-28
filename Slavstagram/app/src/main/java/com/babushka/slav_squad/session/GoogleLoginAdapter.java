package com.babushka.slav_squad.session;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.babushka.slav_squad.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by iliyan on 24.05.17.
 */

public class GoogleLoginAdapter
        implements LoginAdapter, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 562;
    @NonNull
    private final AppCompatActivity mActivity;
    @NonNull
    private final Callback mCallback;
    @NonNull
    private final GoogleApiClient mGoogleApiClient;

    GoogleLoginAdapter(@NonNull AppCompatActivity activity, @NonNull Callback callback) {
        mActivity = activity;
        mCallback = callback;

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by GoogleSignInOptions.
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        handleErrorAndCallback(new GoogleSignInError(connectionResult.getErrorMessage()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                mCallback.onSuccess(account);
            } else {
                handleErrorAndCallback(new GoogleSignInError("Google SignIn failed"));
            }
        }
    }

    private void handleErrorAndCallback(@NonNull GoogleSignInError error) {
        mGoogleApiClient.stopAutoManage(mActivity);
        mGoogleApiClient.disconnect();
        mCallback.onError(error);
    }

    interface Callback {
        void onSuccess(@Nullable GoogleSignInAccount account);

        void onError(@NonNull GoogleSignInError error);
    }

    static class GoogleSignInError extends RuntimeException {
        GoogleSignInError(@Nullable String message) {
            super(message);
        }
    }
}
