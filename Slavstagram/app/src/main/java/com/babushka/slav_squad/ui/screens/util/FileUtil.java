package com.babushka.slav_squad.ui.screens.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by iliyan on 10.08.17.
 */

public class FileUtil {
    @NonNull
    public static File createPublicImageFile(@NonNull Context context) throws IOException {
        // Get the directory for the app's private pictures directory.
        File imagesDirectory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Slav Squad");
        if (!imagesDirectory.mkdirs()) {
            Timber.e("Pictures directory not created");
        }
        return createTempFile(imagesDirectory, FileType.JPEG);
    }

    @NonNull
    public static File createTempPrivateFile(@NonNull Context context, @NonNull FileType fileType) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return createTempFile(storageDir, fileType);
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    private static File createTempFile(@Nullable File directory, @NonNull FileType fileType) throws IOException {
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
        String fileName = prefix + timeStamp + "_";
        return File.createTempFile(
                fileName,  /* file name */
                extension,         /* extension */
                directory      /* directory */
        );
    }

    public static void deleteFile(@NonNull Uri fileUri) {
        deleteFile(fileUri.getPath());
    }

    public static void deleteFile(@NonNull String filePath) {
        deleteFile(new File(filePath));
    }

    public static void deleteFile(@NonNull File file) {
        if (file.exists() && !file.delete()) {
            Timber.d("Error while deleting file: %s", file.getAbsolutePath());
        }
    }

    public enum FileType {
        JPEG,
        PNG
    }
}
