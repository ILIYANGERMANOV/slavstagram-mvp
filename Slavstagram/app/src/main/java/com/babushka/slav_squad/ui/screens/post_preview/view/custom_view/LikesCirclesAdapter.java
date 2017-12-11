package com.babushka.slav_squad.ui.screens.post_preview.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.container.GlideAdapter;

/**
 * Created by iliyan on 22.11.17.
 */

public class LikesCirclesAdapter extends GlideAdapter<UserBase, LikeCircleViewHolder> {
    LikesCirclesAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader) {
        super(context, imageLoader);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_like_circle_view_holder;
    }

    @Override
    protected LikeCircleViewHolder initializeViewHolder(@NonNull View layoutView) {
        return new LikeCircleViewHolder(layoutView, mImageLoader);
    }
}