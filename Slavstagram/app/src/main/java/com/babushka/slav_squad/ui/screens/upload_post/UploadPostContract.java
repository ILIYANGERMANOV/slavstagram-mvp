package com.babushka.slav_squad.ui.screens.upload_post;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.BaseView;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.upload_post.model.UploadPostModel;

import java.io.File;
import java.io.IOException;

/**
 * Created by iliyan on 03.06.17.
 */

public interface UploadPostContract {
    interface View extends BaseView {
        void displayPostImage(@NonNull Uri photoUri);

        void openCameraWithPermissionCheck(int requestCode);

        void openGalleryWithCheck(int requestCode);

        void openImageCropScreen(@NonNull Uri sourceUri, @NonNull Uri destinationUri);

        void setUploading();

        void showError(@NonNull String message);

        void showSuccessAndCloseScreen();

        void closeScreen();
    }

    interface Presenter extends BasePresenter {
        void applyBusinessLogic(boolean useCamera);

        void handleCameraClicked();

        @NonNull
        File providePhotoFile() throws IOException;

        void handleGalleryClicked();

        void handleUpload(@NonNull String description);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {
        void uploadPost(@NonNull Uri imageUri, @NonNull String description,
                        @NonNull UploadPostModel.UploadPostListener listener);

        @Nullable
        Uri getCurrentPhotoFile();

        @NonNull
        File createNewPhotoFile() throws IOException;

        void deleteCurrentPhotoFileIfExists();

        @NonNull
        Uri getSelectedImageFromGallery(@NonNull Intent data)
                throws GalleryResult.SelectedImageNotFoundException;
    }
}
