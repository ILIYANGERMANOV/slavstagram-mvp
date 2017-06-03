package com.babushka.slav_squad.ui.screens.upload_post.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;
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
    //TODO: Refactor by splitting in separate classes
    private static final int RC_IMAGE_CAPTURE = 1;
    @NonNull
    private final UploadPostContract.Model mModel;
    private UploadPostContract.View mView;

    public UploadPostPresenter(@NonNull UploadPostContract.View view,
                               @NonNull UploadPostContract.Model model) {
        mView = view;
        mModel = model;
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
        //TODO: Implement method

    }

    @Override
    public void handleUpload(@NonNull String description) {
        //TODO: Implement method
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_IMAGE_CAPTURE:
                handleCameraResult(resultCode, data);
                break;
            case UCrop.REQUEST_CROP:
                handleCropResult(resultCode, data);
                break;
        }
    }

    private void handleCameraResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            handleCameraOkResult();
        } else if (resultCode == RESULT_CANCELED) {
            mModel.deleteCurrentPhotoFileIfExists();
        }
    }

    private void handleCameraOkResult() {
        Timber.d("Photo taken successfully");
        try {
            cropPhoto(mModel.getCurrentPhotoFile());
        } catch (IOException e) {
            e.printStackTrace();
            mModel.deleteCurrentPhotoFileIfExists();
            mView.showError("Error while trying to crop image");
        }
    }

    private void cropPhoto(@Nullable Uri photoUri) throws IOException {
        if (photoUri != null) {
            File croppedFile = mModel.createCroppedImageFile();
            mView.openImageCropScreen(photoUri, Uri.fromFile(croppedFile));
        } else {
            mView.showError("Selected image Uri is missing, can NOT crop :(");
        }
    }

    private void handleCropResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            handleCropOkResult(data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropErrorResult(data);
        } else if (resultCode == RESULT_CANCELED) {
            mModel.deleteCroppedImageFile();
            mModel.deleteCurrentPhotoFileIfExists();
        }
    }

    private void handleCropOkResult(Intent data) {
        mModel.deleteCurrentPhotoFileIfExists();
        final Uri resultUri = UCrop.getOutput(data);
        if (resultUri != null) {
            mView.displayPostImage(resultUri);
        } else {
            mView.showError("Shit happened while processing crop result");
        }
    }

    private void handleCropErrorResult(Intent data) {
        mModel.deleteCroppedImageFile();
        mModel.deleteCurrentPhotoFileIfExists();
        final Throwable cropError = UCrop.getError(data);
        if (cropError != null) {
            cropError.printStackTrace();
        }
        mView.showError("Error while processing cropped image");
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
