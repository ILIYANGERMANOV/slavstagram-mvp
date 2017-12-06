package com.babushka.slav_squad.ui.screens.edit_profile.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.edit_profile.EditProfileContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by iliyan on 05.12.17.
 */

public class EditProfileModel implements EditProfileContract.Model {
    //TODO: Refactor and implement correctly (handle db update cases)
    @NonNull
    private final Context mContext;

    public EditProfileModel(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public void updateUserProfile(@NonNull final User user, @NonNull Uri newProfilePic, @NonNull final String newDisplayName, @NonNull final UpdateUserCallback callback) {
        Storage.getInstance().uploadImage(newProfilePic, new Storage.UploadImageListener() {
            @Override
            public void onImageUploaded(@NonNull Post.Image image) {
                String newProfilePicUrl = image.getImageUrl();
                user.setPhotoUrl(newProfilePicUrl);
                user.setHighResPhotoUrl(newProfilePicUrl);
                user.setDisplayName(newDisplayName);
                updateUser(user, callback);
            }

            @Override
            public void onError(@NonNull Exception e) {
                e.printStackTrace();
                callback.onError();
            }
        });
    }

    @Override
    public void updateUserDisplayName(@NonNull User user, @NonNull String newDisplayName, @NonNull UpdateUserCallback callback) {
        user.setDisplayName(newDisplayName);
        updateUser(user, callback);
    }


    private void updateUser(@NonNull User user, @NonNull UpdateUserCallback callback) {
        Database.getInstance().updateUser(user);
        updateFirebaseUser(user, callback);
    }

    private void updateFirebaseUser(@NonNull User updatedUser, @NonNull final UpdateUserCallback callback) {
        FirebaseUser user = SessionManager.getInstance().getCurrentFirebaseUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(updatedUser.getDisplayName())
                .setPhotoUri(Uri.parse(updatedUser.getPhotoUrl()))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    }
                });
    }

    @NonNull
    @Override
    public Uri getSelectedImageFromGallery(@NonNull Intent data) throws GalleryResult.SelectedImageNotFoundException {
        GalleryResult galleryResult = new GalleryResult(mContext, data);
        return galleryResult.getSelectedImageFromGallery();
    }
}
