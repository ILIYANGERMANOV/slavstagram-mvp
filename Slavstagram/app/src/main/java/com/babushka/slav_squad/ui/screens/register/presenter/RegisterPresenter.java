package com.babushka.slav_squad.ui.screens.register.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.analytics.SimpleAnalytics;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.session.FirebaseLoginCallback;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.session.data.UserDetails;
import com.babushka.slav_squad.ui.AnalyticsPresenter;
import com.babushka.slav_squad.ui.screens.register.RegisterContract;
import com.babushka.slav_squad.ui.screens.register.view.fragment.RegisterSecondStepFragment;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterPresenter extends AnalyticsPresenter implements RegisterContract.Presenter {
    @NonNull
    private final RegisterContract.Model mModel;
    private RegisterContract.View mView;
    private UserDetails mUserDetails;

    public RegisterPresenter(@NonNull RegisterContract.View view, @NonNull RegisterContract.Model model,
                             @NonNull SimpleAnalytics analytics) {
        super(analytics);
        mView = view;
        mModel = model;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void startRegisterWizard() {
        mAnalytics.logEvent(Events.Register.REG_WIZARD_START);
        mView.showFirstStep();
    }

    @Override
    public void handleFirstStepCompleted(@NonNull LoginDetails loginDetails) {
        mAnalytics.logEvent(Events.Register.FIRST_STEP_COMPLETE);
        mUserDetails = new UserDetails(loginDetails.getEmail(), loginDetails.getPassword());
        mView.showSecondStep();
    }

    @Override
    public void handleSecondStepCompleted(@NonNull RegisterSecondStepFragment.Input input) {
        mView.setLoading();
        mAnalytics.logEvent(Events.Register.REG_WITH_EMAIL);
        mUserDetails.setDisplayName(input.getDisplayName());
        Uri profilePictureUri = input.getPhotoUri();
        if (profilePictureUri == null) {
            //Randomize profile photo
            String profileImageUrl = mModel.getRandomProfileImageUrl();
            registerUser(profileImageUrl);
            return;
        }
        mModel.uploadProfilePhoto(profilePictureUri, new Storage.UploadImageListener() {
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
                mAnalytics.logEvent(Events.Register.REG_WITH_EMAIL_SUCCESS);
                mModel.saveUser(user);
                if (mView != null) {
                    mView.restartApp();
                }
            }

            @Override
            public void onError(@Nullable Exception exception) {
                mAnalytics.logEvent(Events.Register.REG_WITH_EMAIL_ERROR);
                if (mView != null && exception != null) {
                    mView.showError(exception.getMessage());
                }
            }
        });
    }
}
