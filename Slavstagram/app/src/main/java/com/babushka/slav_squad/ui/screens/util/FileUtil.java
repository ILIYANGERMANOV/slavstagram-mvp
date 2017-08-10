package com.babushka.slav_squad.ui.screens.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by iliyan on 10.08.17.
 */

public class FileUtil {
    @SuppressLint("SimpleDateFormat")
    @NonNull
    public static File createTempFile(@NonNull Context context, @NonNull FileType fileType) throws IOException {
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
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* file name */
                extension,         /* extension */
                storageDir      /* directory */
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
