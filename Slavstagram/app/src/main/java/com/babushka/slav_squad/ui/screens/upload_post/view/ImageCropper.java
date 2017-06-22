package com.babushka.slav_squad.ui.screens.upload_post.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.babushka.slav_squad.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

/**
 * Created by iliyan on 22.06.17.
 */

public class ImageCropper {
    public static final int MAX_RESULT_SIZE = 600;
    @NonNull
    private final Activity mActivity;

    public ImageCropper(@NonNull Activity activity) {
        mActivity = activity;
    }

    public void crop(@NonNull Uri sourceUri, @NonNull Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        setColorOptions(options);
        setControlOptions(options);
        setCompressionOptions(options);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(mActivity);
    }

    private void setColorOptions(@NonNull UCrop.Options options) {
        options.setStatusBarColor(ContextCompat.getColor(mActivity, R.color.colorPrimaryDark));
        options.setActiveWidgetColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
        options.setToolbarColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        options.setToolbarWidgetColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
        options.setLogoColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
    }

    private void setControlOptions(@NonNull UCrop.Options options) {
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ALL, UCropActivity.SCALE);
        options.setAspectRatioOptions(1, new AspectRatio("4:3", 4, 3),
                new AspectRatio("1:1", 1, 1), new AspectRatio("3:4", 3, 4));
    }

    private void setCompressionOptions(@NonNull UCrop.Options options) {
        options.withMaxResultSize(MAX_RESULT_SIZE, MAX_RESULT_SIZE);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
    }
}
