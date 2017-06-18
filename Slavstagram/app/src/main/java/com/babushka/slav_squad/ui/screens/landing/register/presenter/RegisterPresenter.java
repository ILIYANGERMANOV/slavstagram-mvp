package com.babushka.slav_squad.ui.screens.landing.register.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.session.FirebaseLoginCallback;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.session.data.UserDetails;
import com.babushka.slav_squad.ui.screens.landing.register.RegisterContract;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterPresenter implements RegisterContract.Presenter {
    @NonNull
    private final RegisterContract.Model mModel;
    private RegisterContract.View mView;
    private UserDetails mUserDetails;

    public RegisterPresenter(@NonNull RegisterContract.View view, @NonNull RegisterContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void startRegisterWizard() {
        mView.showFirstStep();
    }

    @Override
    public void handleUserDetailsEntered(@NonNull UserDetails userDetails) {
        mUserDetails = userDetails;
        mView.showSecondStep();
    }

    @Override
    public void handleProfilePhotoSelected(@Nullable Uri photoPath) {
        mView.setLoading();
        if (photoPath == null) {
            //Randomize profile photo
            String profileImageUrl = mModel.getRandomProfileImageUrl();
            registerUser(profileImageUrl);
            return;
        }
        mModel.uploadProfilePhoto(photoPath, new Storage.UploadImageListener() {
            @Override
            public void onImageUploaded(@NonNull Post.Image image) {
                registerUser(image.getImageUrl());
            }

            @Override
            public void onError(@NonNull Exception e) {
                if (mView != null) {
                    mView.showError("Error while uploading profile picture");
                }
            }
        });
    }

    private void registerUser(@NonNull String photoUrl) {
        mUserDetails.setPhotoUrl(photoUrl);
        SessionManager.getInstance().registerUser(mUserDetails, new FirebaseLoginCallback() {
            @Override
            public void onSuccess(@NonNull FirebaseUser user) {
                mModel.saveUser(user);
                if (mView != null) {
                    mView.restartApp();
                }
            }

            @Override
            public void onError(@Nullable Exception exception) {
                if (mView != null && exception != null) {
                    mView.showError(exception.getMessage());
                }
            }
        });
    }
}
