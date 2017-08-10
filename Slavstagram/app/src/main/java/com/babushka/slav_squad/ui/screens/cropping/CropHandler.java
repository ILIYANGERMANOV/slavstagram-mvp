package com.babushka.slav_squad.ui.screens.cropping;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.ui.screens.util.FileUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by iliyan on 06.06.17.
 */

public class CropHandler {
    //TODO: Refactor
    @NonNull
    private final Context mContext;

    @Nullable
    private String mCroppedImageDestPath;
    @Nullable
    private Uri mCroppedImage;

    private Result mCallback;

    public CropHandler(@NonNull Context context) {
        mContext = context;
    }

    @Nullable
    public Uri getCroppedImage() {
        return mCroppedImage;
    }

    public void cropPhoto(@Nullable Uri sourceUri, @NonNull Result callback) {
        mCallback = callback;
        if (sourceUri != null) {
            initiateCropping(sourceUri);
        } else {
            mCallback.onError("Selected image Uri is missing, can NOT crop :(");
        }
    }

    private void initiateCropping(@NonNull Uri sourceUri) {
        try {
            File destinationFile = createCroppedImageFile();
            mCallback.onStartCropActivity(sourceUri, Uri.fromFile(destinationFile));
        } catch (IOException e) {
            e.printStackTrace();
            mCallback.onDeleteCurrentPhotoFile();
            mCallback.onError("IOException while creating cropped photo file");
        }
    }

    @NonNull
    private File createCroppedImageFile() throws IOException {
        File photoFile = FileUtil.createTempFile(mContext, FileUtil.FileType.PNG);
        mCroppedImageDestPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    public void handleCropResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            handleCropOkResult(data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropErrorResult(data);
        } else if (resultCode == RESULT_CANCELED) {
            deleteCroppedImageFile();
            mCallback.onDeleteCurrentPhotoFile();
        }
    }

    private void handleCropOkResult(Intent data) {
        mCallback.onDeleteCurrentPhotoFile();
        mCroppedImage = UCrop.getOutput(data);
        if (mCroppedImage != null) {
            mCallback.onCropped(mCroppedImage);
        } else {
            mCallback.onError("Shit happened while processing crop result");
        }
    }

    private void handleCropErrorResult(Intent data) {
        deleteCroppedImageFile();
        mCallback.onDeleteCurrentPhotoFile();
        final Throwable cropError = UCrop.getError(data);
        if (cropError != null) {
            cropError.printStackTrace();
        }
        mCallback.onError("Error while processing cropped image");
    }

    private void deleteCroppedImageFile() {
        if (mCroppedImageDestPath != null) {
            FileUtil.deleteFile(mCroppedImageDestPath);
        }
    }

    public interface Result {
        void onStartCropActivity(@NonNull Uri sourceUri, @NonNull Uri destinationUri);

        void onCropped(@NonNull Uri croppedImage);

        void onError(@NonNull String message);

        void onDeleteCurrentPhotoFile();
    }
}
