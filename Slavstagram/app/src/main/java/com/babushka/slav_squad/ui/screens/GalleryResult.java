package com.babushka.slav_squad.ui.screens;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.util.AppUtil;

/**
 * Created by iliyan on 18.06.17.
 */

public class GalleryResult {

    private final Context mContext;
    private final Intent mData;

    public GalleryResult(@NonNull Context context, @NonNull Intent data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    public Uri getSelectedImageFromGallery() throws SelectedImageNotFoundException {
        ContentResolver contentResolver = mContext.getContentResolver();
        //TODO: Ensure all cases are handled properly
        if (AppUtil.isPreKitkat()) {
            return getGalleryImageUriPreKitkat(contentResolver);
        } else {
            return getGalleryImageUriAfterKitkat();
        }
    }

    @NonNull
    private Uri getGalleryImageUriPreKitkat(ContentResolver contentResolver) throws SelectedImageNotFoundException {
        Uri originalUri = mData.getData();
        String[] projection = {MediaStore.Images.Media.DATA};
        if (originalUri == null) {
            throw new SelectedImageNotFoundException();
        }

        Cursor cursor = contentResolver.query(originalUri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            return Uri.parse(picturePath);
        }
        return originalUri;
    }


    @NonNull
    private Uri getGalleryImageUriAfterKitkat() throws SelectedImageNotFoundException {
        Uri originalUri = mData.getData();
        if (originalUri == null) {
            throw new SelectedImageNotFoundException();
        }
        return originalUri;
    }

    public static class SelectedImageNotFoundException extends Exception {
    }
}
