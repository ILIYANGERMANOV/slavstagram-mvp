package com.babushka.slav_squad.ui.screens.edit_profile.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.event.UpdateProfileEvent;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.cropping.CropHandler;
import com.babushka.slav_squad.ui.screens.edit_profile.EditProfileContract;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import static android.app.Activity.RESULT_OK;

/**
 * Created by iliyan on 05.12.17.
 */

public class EditProfilePresenter implements EditProfileContract.Presenter {
    private final static int RC_GALLERY = 1;

    @NonNull
    private final EditProfileContract.Model mModel;
    @NonNull
    private final User mUser;
    private EditProfileContract.View mView;
    @NonNull
    private final CropHandler mCropHandler;

    @Nullable
    private Uri mCroppedImage;

    public EditProfilePresenter(@NonNull EditProfileContract.View view, @NonNull EditProfileContract.Model model,
                                @NonNull User user, @NonNull CropHandler cropHandler) {
        mView = view;
        mModel = model;
        mUser = user;
        mCropHandler = cropHandler;
    }

    @Override
    public void setupUI() {
        mView.displayProfilePic(mUser.getPhotoUrl());
        mView.displayName(mUser.getDisplayName());
    }

    @Override
    public void handleChangePhotoClick() {
        mView.openGalleryWithCheck(RC_GALLERY);
    }

    @Override
    public void saveChanges(@NonNull String displayName) {
        mView.showSaveProgress();
        EditProfileContract.Model.UpdateUserCallback updateProfileCallback = new EditProfileContract.Model.UpdateUserCallback() {
            @Override
            public void onSuccess() {
                if (mView != null) {
                    mView.dismissProgress();
                    EventBus.getDefault().post(new UpdateProfileEvent());
                    setResultOkAndCloseScreen();
                }
            }

            @Override
            public void onError() {
                if (mView != null) {
                    mView.dismissProgress();
                    mView.showToast("Error while saving changes");
                }
            }
        };
        if (mCroppedImage != null) {
            mModel.updateUserProfile(mUser, mCroppedImage, displayName, updateProfileCallback);
        } else {
            mModel.updateUserDisplayName(mUser, displayName, updateProfileCallback);
        }
    }

    private void setResultOkAndCloseScreen() {
        mView.setResultOK();
        mView.closeScreen();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_GALLERY:
                handleGalleryResult(resultCode, data);
                break;
            case UCrop.REQUEST_CROP:
                mCropHandler.handleCropResult(resultCode, data);
                break;
        }
    }

    private void handleGalleryResult(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImage = mModel.getSelectedImageFromGallery(data);
                cropPhoto(selectedImage);
            } catch (GalleryResult.SelectedImageNotFoundException e) {
                e.printStackTrace();
                mView.showToast("Selected image path not found :(");
            }
        }
    }

    private void cropPhoto(Uri photo) {
        mCropHandler.cropPhoto(photo, new CropHandler.Result() {
            @Override
            public void onStartCropActivity(@NonNull Uri sourceUri, @NonNull Uri destinationUri) {
                if (mView != null) {
                    mView.openImageCropScreen(sourceUri, destinationUri);
                }
            }

            @Override
            public void onCropped(@NonNull Uri croppedImage) {
                if (mView != null) {
                    mCroppedImage = croppedImage;
                    mView.displayProfilePic(croppedImage.getPath());
                }
            }

            @Override
            public void onError(@NonNull String message) {
                if (mView != null) {
                    mView.showToast(message);
                }
            }

            @Override
            public void onDeleteCurrentPhotoFile() {
                //do nothing, we use only gallery
            }
        });
    }

    @Override
    public void onDestroy() {
        mView.dismissProgress();
        mView = null;
    }
}
