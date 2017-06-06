package com.babushka.slav_squad.ui.screens.upload_post.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;
import com.babushka.slav_squad.ui.screens.upload_post.model.UploadPostModel;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by iliyan on 03.06.17.
 */

public class UploadPostPresenter implements UploadPostContract.Presenter {
    private static final int RC_IMAGE_CAPTURE = 1;
    private static final int RC_GALLERY = 2;

    @NonNull
    private final UploadPostContract.Model mModel;
    @NonNull
    private final CropHandler mCropHandler;
    private UploadPostContract.View mView;

    public UploadPostPresenter(@NonNull UploadPostContract.View view,
                               @NonNull UploadPostContract.Model model) {
        mView = view;
        mModel = model;
        mCropHandler = new CropHandler(view, model);
    }

    @Override
    public void handleCameraClicked() {
        mView.openCameraWithPermissionCheck(RC_IMAGE_CAPTURE);
    }

    @NonNull
    @Override
    public File providePhotoFile() throws IOException {
        return mModel.createNewPhotoFile();
    }

    @Override
    public void handleGalleryClicked() {
        mView.openGalleryWithCheck(RC_GALLERY);
    }

    @Override
    public void handleUpload(@NonNull String description) {
        Uri croppedImageUri = mCropHandler.getCroppedImageUri();
        if (croppedImageUri != null) {
            mView.setUploading();
            mModel.uploadPost(croppedImageUri, description, new UploadPostModel.UploadPostListener() {
                @Override
                public void onPostUploaded() {
                    if (mView != null) {
                        mView.showSuccessAndCloseScreen();
                    }
                }

                @Override
                public void onError() {
                    if (mView != null) {
                        mView.showError("Error while uploading post");
                    }
                }
            });
        } else {
            mView.showError("Cannot upload post without image :)");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_IMAGE_CAPTURE:
                handleCameraResult(resultCode);
                break;
            case RC_GALLERY:
                handleGalleryResult(resultCode, data);
                break;
            case UCrop.REQUEST_CROP:
                mCropHandler.handleCropResult(resultCode, data);
                break;
        }
    }

    private void handleCameraResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            handleCameraOkResult();
        } else if (resultCode == RESULT_CANCELED) {
            mModel.deleteCurrentPhotoFileIfExists();
        }
    }

    private void handleCameraOkResult() {
        Timber.d("Photo taken successfully");
        try {
            mCropHandler.cropPhoto(mModel.getCurrentPhotoFile());
        } catch (IOException e) {
            e.printStackTrace();
            mModel.deleteCurrentPhotoFileIfExists();
            mView.showError("Error while trying to crop image");
        }
    }

    private void handleGalleryResult(int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImage = mModel.getSelectedImageFromGallery(data);
                mCropHandler.cropPhoto(selectedImage);
            } catch (UploadPostModel.SelectedImageNotFoundException e) {
                e.printStackTrace();
                mView.showError("Selected image path not found :(");
            } catch (IOException e) {
                e.printStackTrace();
                mView.showError("Error while trying to crop image");
            }
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
