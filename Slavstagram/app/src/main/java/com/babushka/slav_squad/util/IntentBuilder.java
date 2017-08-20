package com.babushka.slav_squad.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.babushka.slav_squad.R;

import java.io.File;

/**
 * Created by iliyan on 18.06.17.
 */

public class IntentBuilder {
    @NonNull
    public static Intent buildOpenCameraIntent(@NonNull Context context, @NonNull File photoFile)
            throws ResolveActivityException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI;
        if (AppUtil.isPreNougat()) {
            photoURI = Uri.fromFile(photoFile);
        } else {
            photoURI = FileProvider.getUriForFile(context,
                    context.getString(R.string.file_provider_authority),
                    photoFile);
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        resolveActivity(context, takePictureIntent);
        return takePictureIntent;
    }

    @NonNull
    public static Intent buildOpenGalleryIntent(@NonNull Context context)
            throws ResolveActivityException {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        resolveActivity(context, chooserIntent);

        return chooserIntent;
    }

    @NonNull
    public static Intent buildShareIntent(@NonNull String textToShare) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        shareIntent.setType("text/plain");
        return Intent.createChooser(shareIntent, "Shave via");
    }

    private static void resolveActivity(@NonNull Context context, Intent intent)
            throws ResolveActivityException {
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            throw new ResolveActivityException();
        }
    }

    public static class ResolveActivityException extends Exception {
    }
}
