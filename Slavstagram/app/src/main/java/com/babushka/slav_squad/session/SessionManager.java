package com.babushka.slav_squad.session;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

    public FacebookLoginAdapter loginWithFacebook(@NonNull final Activity activity, @NonNull LoginButton loginButton,
                                                  @NonNull final FacebookLoginCallback loginCallback) {
        FacebookLoginAdapter loginAdapter = new FacebookLoginAdapter(loginButton);
        loginAdapter.setupLoginButton(new FacebookLoginAdapter.Callback() {
            @Override
            public void onSuccess(@NonNull AuthCredential credential) {
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                                    loginCallback.onSuccess(mAuth.getCurrentUser());
                                } else {
                                    loginCallback.onError(task.getException());
                                }
                            }
                        });
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

    public void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }
}
