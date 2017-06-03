package com.babushka.slav_squad.ui.screens.upload_post;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.BasePresenter;
import com.babushka.slav_squad.ui.screens.BaseView;

import java.io.File;
import java.io.IOException;

/**
 * Created by iliyan on 03.06.17.
 */

public interface UploadPostContract {
    interface View extends BaseView {
        void displayPostImage(@NonNull Uri photoUri);

        void openCameraWithPermissionCheck(int requestCode);

        void openGallery(int requestCode);

        void openImageCropScreen(@NonNull Uri sourceUri, @NonNull Uri destinationUri);

        void showError(@NonNull String message);
    }

    interface Presenter extends BasePresenter {
        void handleCameraClicked();

        @NonNull
        File providePhotoFile() throws IOException;

        void handleGalleryClicked();

        void handleUpload(@NonNull String description);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {
        void uploadPost(@NonNull Post post);

        @Nullable
        Uri getCurrentPhotoFile();

        @NonNull
        File createNewPhotoFile() throws IOException;

        void deleteCurrentPhotoFile();

        @NonNull
        File createCroppedImageFile() throws IOException;

        void deleteCroppedImageFile();
    }
}
