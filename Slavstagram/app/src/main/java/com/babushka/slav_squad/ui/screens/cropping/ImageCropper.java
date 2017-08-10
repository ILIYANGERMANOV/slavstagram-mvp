package com.babushka.slav_squad.ui.screens.cropping;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.babushka.slav_squad.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

/**
 * Created by iliyan on 22.06.17.
 */

public class ImageCropper {
    private final int mMaxResultSize;

    @NonNull
    private final Context mContext;
    @NonNull
    private final UCrop.Options mOptions;
    @NonNull
    private final AspectRatioOption mAspectRatioOption;


    ImageCropper(@NonNull Context context, int maxResultSize, @NonNull AspectRatioOption aspectRatioOption) {
        mContext = context;
        mMaxResultSize = maxResultSize;
        mAspectRatioOption = aspectRatioOption;

        mOptions = new UCrop.Options();
        setColorOptions(mOptions);
        setControlOptions(mOptions);
        setCompressionOptions(mOptions);
    }

    public void crop(@NonNull Activity activity, @NonNull Uri sourceUri, @NonNull Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
                .withOptions(mOptions)
                .start(activity);
    }

    public void crop(@NonNull Fragment fragment, @NonNull Uri sourceUri, @NonNull Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
                .withOptions(mOptions)
                .start(mContext, fragment);
    }

    private void setColorOptions(@NonNull UCrop.Options options) {
        options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        options.setActiveWidgetColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        options.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        options.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        options.setLogoColor(ContextCompat.getColor(mContext, R.color.colorAccent));
    }

    private void setControlOptions(@NonNull UCrop.Options options) {
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ALL, UCropActivity.SCALE);
        options.setAspectRatioOptions(mAspectRatioOption.getSelectedByDefault(), mAspectRatioOption.getAspectRatios());
    }

    private void setCompressionOptions(@NonNull UCrop.Options options) {
        options.withMaxResultSize(mMaxResultSize, mMaxResultSize);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
    }

    static class AspectRatioOption {
        @NonNull
        private final AspectRatio[] mAspectRatios;
        private int mSelectedByDefault;

        public AspectRatioOption(int selectedByDefault, @NonNull AspectRatio... aspectRatios) {
            mSelectedByDefault = selectedByDefault;
            mAspectRatios = aspectRatios;
        }

        int getSelectedByDefault() {
            return mSelectedByDefault;
        }

        @NonNull
        AspectRatio[] getAspectRatios() {
            return mAspectRatios;
        }
    }
}
