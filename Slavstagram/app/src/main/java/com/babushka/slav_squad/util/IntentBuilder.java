package com.babushka.slav_squad.util;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

/**
 * Created by iliyan on 18.06.17.
 */

public class IntentBuilder {
    public static Intent buildOpenCameraIntent(@NonNull Context context)
            throws ResolveActivityException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        resolveActivity(context, takePictureIntent);
        return takePictureIntent;
    }

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

    private static void resolveActivity(@NonNull Context context, Intent intent)
            throws ResolveActivityException {
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            throw new ResolveActivityException();
        }
    }

    public static class ResolveActivityException extends Exception {
    }
}
