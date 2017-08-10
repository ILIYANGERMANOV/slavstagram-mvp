package com.babushka.slav_squad.ui.screens.cropping;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yalantis.ucrop.model.AspectRatio;

/**
 * Created by iliyan on 10.08.17.
 */

public class ProfilePictureCropper extends ImageCropper {
    private static final int MAX_RESULT_SIZE = 200;

    public ProfilePictureCropper(@NonNull Context context) {
        super(context, MAX_RESULT_SIZE, buildAspectRatioOption());
    }

    @NonNull
    private static AspectRatioOption buildAspectRatioOption() {
        return new AspectRatioOption(0, new AspectRatio("1:1", 1, 1));
    }
}
