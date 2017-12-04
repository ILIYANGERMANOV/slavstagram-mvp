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
        display(image, imageLoader, false);
    }

    public void display(@NonNull final Post.Image image, @NonNull final GlideRequests imageLoader,
                        final boolean roundCorners) {
        int measuredWidth = getMeasuredWidth();
        if (measuredWidth == 0) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    resizeAndLoadImage(image, imageLoader, getMeasuredWidth(), roundCorners);
                }
            });
        } else {
            resizeAndLoadImage(image, imageLoader, measuredWidth, roundCorners);
        }
    }

    private void resizeAndLoadImage(@NonNull Post.Image postImage, @NonNull GlideRequests imageLoader,
                                    int width, boolean roundCorners) {
        int calculatedHeight = (int) (width / postImage.getWidthHeightRatio());
        setViewHeight(calculatedHeight);
        GlideRequest<Drawable> request = imageLoader.load(postImage.getImageUrl())
                .override(width, calculatedHeight);
        if (roundCorners) {
            request.transform(new RoundedCorners(AppUtil.dpToPx(getContext(), 2)));
        }
        //TODO: add placeholder and error drawable
        request.into(this);
    }

    private void setViewHeight(int height) {
        getLayoutParams().height = height;
        requestLayout();
    }
}
