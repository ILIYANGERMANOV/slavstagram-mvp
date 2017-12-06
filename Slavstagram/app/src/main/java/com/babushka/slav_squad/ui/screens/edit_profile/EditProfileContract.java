package com.babushka.slav_squad.ui.screens.edit_profile;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.BaseView;
import com.babushka.slav_squad.ui.screens.GalleryResult;

/**
 * Created by iliyan on 05.12.17.
 */

public interface EditProfileContract {
    interface View extends BaseView {
        void displayProfilePic(@NonNull String photoUrl);

        void displayName(@NonNull String displayName);

        void openGalleryWithCheck(int requestCode);

        void openImageCropScreen(@NonNull Uri sourceUri, @NonNull Uri destinationUri);

        void showSaveProgress();

        void dismissProgress();

        void setResultOK();

        void closeScreen();

        void showToast(@NonNull String message);
    }

    interface Presenter extends BasePresenter {
        void setupUI();

        void handleChangePhotoClick();

        void saveChanges(@NonNull String displayName);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {
        void updateUserProfile(@NonNull User user, @NonNull Uri newProfilePic,
                               @NonNull String newDisplayName, @NonNull UpdateUserCallback callback);

        void updateUserDisplayName(@NonNull User user, @NonNull String newDisplayName, @NonNull UpdateUserCallback callback);

        @NonNull
        Uri getSelectedImageFromGallery(@NonNull Intent data)
                throws GalleryResult.SelectedImageNotFoundException;

        interface UpdateUserCallback {
            void onSuccess();

            void onError();
        }
    }
}
