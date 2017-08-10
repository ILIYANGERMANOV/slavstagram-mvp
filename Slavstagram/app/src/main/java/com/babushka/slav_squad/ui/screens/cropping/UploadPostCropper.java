package com.babushka.slav_squad.ui.screens.cropping;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yalantis.ucrop.model.AspectRatio;

/**
 * Created by iliyan on 10.08.17.
 */

public class UploadPostCropper extends ImageCropper {
    private static final int MAX_RESULT_SIZE = 600;

    public UploadPostCropper(@NonNull Context context) {
        super(context, MAX_RESULT_SIZE, buildAspectRatioOption());
    }

    @NonNull
    private static ImageCropper.AspectRatioOption buildAspectRatioOption() {
        return new AspectRatioOption(1, new AspectRatio("4:3", 4, 3),
                new AspectRatio("1:1", 1, 1), new AspectRatio("3:4", 3, 4));
    }
}
