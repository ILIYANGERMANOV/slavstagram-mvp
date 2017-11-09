package com.babushka.slav_squad.ui.screens.likes.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.container.GlideAdapter;

/**
 * Created by iliyan on 09.11.17.
 */

public class LikesAdapter extends GlideAdapter<User, LikeViewHolder> {
    protected LikesAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader) {
        super(context, imageLoader);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_like_view_holder;
    }

    @Override
    protected LikeViewHolder initializeViewHolder(@NonNull View layoutView) {
        return new LikeViewHolder(layoutView, mImageLoader);
    }
}
