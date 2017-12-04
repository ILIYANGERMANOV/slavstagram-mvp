package com.babushka.slav_squad.ui.custom_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.babushka.slav_squad.GlideRequest;
import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.util.AppUtil;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

/**
 * Created by iliyan on 21.09.17.
 */

public class AspectRatioImageView extends AppCompatImageView {
    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.FIT_XY);
    }

    public void display(@NonNull final Post.Image image, @NonNull final GlideRequests imageLoader) {
        int measuredWidth = getMeasuredWidth();
        if (measuredWidth == 0) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    resizeAndLoadImage(image, imageLoader, getMeasuredWidth());
                }
            });
        } else {
            resizeAndLoadImage(image, imageLoader, measuredWidth);
        }
    }

    private void resizeAndLoadImage(@NonNull Post.Image postImage, @NonNull GlideRequests imageLoader,
                                    int width) {
        int calculatedHeight = (int) (width / postImage.getWidthHeightRatio());
        setViewHeight(calculatedHeight);
        imageLoader.load(postImage.getImageUrl())
                .override(width, calculatedHeight)
                //TODO: add placeholder and error drawable
                .into(this);
    }

    private void setViewHeight(int height) {
        getLayoutParams().height = height;
        requestLayout();
    }
}
