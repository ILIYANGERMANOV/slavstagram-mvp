package com.babushka.slav_squad.ui.screens.upload_post.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by iliyan on 06.06.17.
 */

class CropHandler {

    private UploadPostContract.View mView;
    @NonNull
    private final UploadPostContract.Model mModel;
    @Nullable
    private Uri mCroppedImageUri;

    CropHandler(@NonNull UploadPostContract.View view, @NonNull UploadPostContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Nullable
    Uri getCroppedImageUri() {
        return mCroppedImageUri;
    }

    void cropPhoto(@Nullable Uri photoUri) throws IOException {
        if (photoUri != null) {
            File croppedFile = mModel.createCroppedImageFile();
            mView.openImageCropScreen(photoUri, Uri.fromFile(croppedFile));
        } else {
            mView.showError("Selected image Uri is missing, can NOT crop :(");
        }
    }

    void handleCropResult(int resultCode, Intent data) {
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
        mCroppedImageUri = UCrop.getOutput(data);
        if (mCroppedImageUri != null) {
            mView.displayPostImage(mCroppedImageUri);
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

    void destroy() {
        mView = null;
    }
}
