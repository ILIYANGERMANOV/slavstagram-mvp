package com.babushka.slav_squad.session;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by iliyan on 21.05.17.
 */

public class SessionManager {
    private static SessionManager sInstance;
    @NonNull
    private final FirebaseAuth mAuth;

    private SessionManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

    public boolean isLoggedUser() {
        return mAuth.getCurrentUser() != null;
    }

    public LoginAdapter loginWithFacebook(@NonNull final Activity activity, @NonNull LoginButton loginButton,
                                          @NonNull final FacebookLoginCallback loginCallback) {
        FacebookLoginAdapter loginAdapter = new FacebookLoginAdapter(loginButton);
        loginAdapter.setupLoginButton(new FacebookLoginAdapter.Callback() {
            @Override
            public void onSuccess(@NonNull AuthCredential credential) {
                //TODO: handle case where user has already signed with G+, signs out and then try with FB
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(activity, new DefaultSignInCompleteListener(loginCallback));
            }

            @Override
            public void onCancel() {
                loginCallback.onCancel();
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                error.printStackTrace();
                loginCallback.onError(error);
            }
        });
        return loginAdapter;
    }

    public LoginAdapter loginWithGoogle(@NonNull final AppCompatActivity activity, @NonNull final FirebaseLoginCallback loginCallback) {
        GoogleLoginAdapter loginAdapter = new GoogleLoginAdapter(activity, new GoogleLoginAdapter.Callback() {
            @Override
            public void onSuccess(@Nullable GoogleSignInAccount account) {
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(activity, new DefaultSignInCompleteListener(loginCallback));

                } else {
                    loginCallback.onError(new Exception("Google login failed: GoogleSignIn account is null"));
                }
            }

            @Override
            public void onError(@NonNull GoogleLoginAdapter.GoogleSignInError error) {
                loginCallback.onError(error);
            }
        });
        loginAdapter.signIn();
        return loginAdapter;
    }

    public void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    private class DefaultSignInCompleteListener implements OnCompleteListener<AuthResult> {
        @NonNull
        private final FirebaseLoginCallback mLoginCallback;

        DefaultSignInCompleteListener(@NonNull FirebaseLoginCallback loginCallback) {
            mLoginCallback = loginCallback;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                mLoginCallback.onSuccess(mAuth.getCurrentUser());
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("SessionManager", exception.getMessage());
                }
                mLoginCallback.onError(exception);
            }
        }
    }
}
