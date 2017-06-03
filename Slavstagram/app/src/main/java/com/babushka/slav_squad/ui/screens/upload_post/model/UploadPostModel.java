package com.babushka.slav_squad.ui.screens.upload_post.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by iliyan on 03.06.17.
 */

public class UploadPostModel implements UploadPostContract.Model {
    @NonNull
    private final Context mContext;
    @Nullable
    private Uri mPhotoPath;
    @Nullable
    private String mCroppedImagePath;

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
        File photoFile = createTempFile(FileType.JPEG);
        mPhotoPath = Uri.fromFile(photoFile);
        return photoFile;
    }

    @Override
    public void deleteCurrentPhotoFileIfExists() {
        if (mPhotoPath != null) {
            File file = new File(mPhotoPath.getPath());
            if (file.exists()) {
                deleteFile(file);
            }
        }
    }

    @NonNull
    @Override
    public File createCroppedImageFile() throws IOException {
        File photoFile = createTempFile(FileType.PNG);
        mCroppedImagePath = photoFile.getAbsolutePath();
        return photoFile;
    }

    @Override
    public void deleteCroppedImageFile() {
        if (mCroppedImagePath != null) {
            deleteFile(new File(mCroppedImagePath));
        }
    }

    private void deleteFile(@NonNull File file) {
        if (!file.delete()) {
            Timber.d("Error while deleting file: %s", file.getAbsolutePath());
        }
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    private File createTempFile(@NonNull FileType fileType) throws IOException {
        String prefix;
        String extension;
        switch (fileType) {
            case JPEG:
                prefix = "JPEG_";
                extension = ".jpg";
                break;
            case PNG:
                prefix = "PNG_";
                extension = ".png";
                break;
            default:
                throw new IllegalArgumentException("Unsupported file type");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = prefix + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* file name */
                extension,         /* extension */
                storageDir      /* directory */
        );
    }

    @Override
    public void uploadPost(@NonNull Post post) {
        Database.getInstance().saveNewPost(post);
    }

    private enum FileType {
        JPEG,
        PNG
    }
}
