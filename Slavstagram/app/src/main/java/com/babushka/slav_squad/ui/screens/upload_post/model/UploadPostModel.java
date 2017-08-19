package com.babushka.slav_squad.ui.screens.upload_post.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;
import com.babushka.slav_squad.ui.screens.util.FileUtil;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

/**
 * Created by iliyan on 03.06.17.
 */

public class UploadPostModel implements UploadPostContract.Model {
    @NonNull
    private final Context mContext;
    @Nullable
    private Uri mPhotoPath;

    public UploadPostModel(@NonNull Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public Uri getCurrentPhotoFile() {
        return mPhotoPath;
    }

    @NonNull
    @Override
    public File createNewPhotoFile() throws IOException {
        File photoFile = FileUtil.createTempPrivateFile(mContext, FileUtil.FileType.PNG);
        mPhotoPath = Uri.fromFile(photoFile);
        return photoFile;
    }

    @Override
    public void deleteCurrentPhotoFileIfExists() {
        if (mPhotoPath != null) {
            FileUtil.deleteFile(mPhotoPath);
        }
    }



    @NonNull
    @Override
    public Uri getSelectedImageFromGallery(@NonNull Intent data) throws GalleryResult.SelectedImageNotFoundException {
        GalleryResult galleryResult = new GalleryResult(mContext, data);
        return galleryResult.getSelectedImageFromGallery();
    }


    @Override
    public void uploadPost(@NonNull final Uri imageUri, @NonNull final String description,
                           @NonNull final UploadPostListener listener) {
        //Upload post image
        Storage.getInstance().uploadImage(imageUri, new Storage.UploadImageListener() {
            @Override
            public void onImageUploaded(@NonNull Post.Image image) {
                savePostInDatabase(image, description, listener);
            }

            @Override
            public void onError(@NonNull Exception e) {
                Timber.d("Error while uploading post image: %s", imageUri.toString());
                listener.onError();
            }
        });
    }

    private void savePostInDatabase(@NonNull Post.Image image, @NonNull String description,
                                    @NonNull final UploadPostListener listener) {
        FirebaseUser currentUser = SessionManager.getInstance().getCurrentFirebaseUser();
        User author = new User(currentUser);
        Post post = new Post(author, description, image);
        Database.getInstance().saveNewPost(post, new Database.OperationListener() {
            @Override
            public void onSuccess() {
                listener.onPostUploaded();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });
    }


    public interface UploadPostListener {
        void onPostUploaded();

        void onError();
    }
}
