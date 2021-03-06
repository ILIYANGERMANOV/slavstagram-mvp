package com.babushka.slav_squad.session;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.session.data.UserDetails;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

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
        return getCurrentFirebaseUser() != null;
    }

    public FirebaseUser getCurrentFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    public User getCurrentUser() {
        return new User(getCurrentFirebaseUser());
    }

    public LoginAdapter loginWithFacebook(@NonNull final Activity activity, @NonNull LoginButton loginButton,
                                          @NonNull final FacebookLoginCallback loginCallback) {
        FacebookLoginAdapter loginAdapter = new FacebookLoginAdapter(loginButton);
        loginAdapter.setupLoginButton(new FacebookLoginAdapter.Callback() {
            @Override
            public void onSuccess(@NonNull AuthCredential credential) {
                //TODO: handle case where user has already signed with G+, signs out and then try with FB
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(activity, new DefaultSignInCompleteListener(loginCallback, new ErrorResolution() {
                            @Override
                            public void onResolveError(@Nullable Exception error) {
                                logoutFacebook();
                                if (error instanceof FirebaseAuthUserCollisionException) {
                                    String message = "Email already associated with Google, login with Google instead!";
                                    loginCallback.onError(new RuntimeException(message));
                                } else {
                                    loginCallback.onError(error);
                                }
                            }
                        }));
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

    public void loginWithEmailAndPassword(@NonNull LoginDetails loginDetails,
                                          @NonNull FirebaseLoginCallback loginCallback) {
        mAuth.signInWithEmailAndPassword(loginDetails.getEmail(), loginDetails.getPassword())
                .addOnCompleteListener(new DefaultSignInCompleteListener(loginCallback));
    }

    public void loginAsGuest(@NonNull FirebaseLoginCallback loginCallback) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(new DefaultSignInCompleteListener(loginCallback));
    }

    public void registerUser(@NonNull final UserDetails userDetails,
                             @NonNull final FirebaseLoginCallback callback) {
        mAuth.createUserWithEmailAndPassword(userDetails.getEmail(), userDetails.getPassword())
                .addOnCompleteListener(new DefaultSignInCompleteListener(new FirebaseLoginCallback() {
                    @Override
                    public void onSuccess(@NonNull FirebaseUser user) {
                        updateUserProfile(user, userDetails, callback);
                    }

                    @Override
                    public void onError(@Nullable Exception exception) {
                        callback.onError(exception);
                    }
                }));
    }

    private void updateUserProfile(@NonNull final FirebaseUser user, @NonNull UserDetails userDetails,
                                   @NonNull final FirebaseLoginCallback callback) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userDetails.getDisplayName())
                .setPhotoUri(Uri.parse(userDetails.getPhotoUrl()))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(user);
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });
    }

    public void logout() {
        mAuth.signOut();
        logoutFacebook();
    }

    private void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

    interface ErrorResolution {
        void onResolveError(@Nullable Exception error);
    }

    private class DefaultSignInCompleteListener implements OnCompleteListener<AuthResult> {
        @NonNull
        private final FirebaseLoginCallback mLoginCallback;
        @Nullable
        private final ErrorResolution mErrorResolution;

        DefaultSignInCompleteListener(@NonNull FirebaseLoginCallback loginCallback,
                                      @NonNull ErrorResolution errorResolution) {
            mLoginCallback = loginCallback;
            mErrorResolution = errorResolution;
        }

        DefaultSignInCompleteListener(@NonNull FirebaseLoginCallback loginCallback) {
            mLoginCallback = loginCallback;
            mErrorResolution = null;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful() && getCurrentFirebaseUser() != null) {
                mLoginCallback.onSuccess(getCurrentFirebaseUser());
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("SessionManager", exception.getMessage());
                }
                if (mErrorResolution != null) {
                    mErrorResolution.onResolveError(exception);
                } else {
                    mLoginCallback.onError(exception);
                }
            }
        }
    }
}
