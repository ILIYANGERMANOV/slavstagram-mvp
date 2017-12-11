package com.babushka.slav_squad.ui.screens.post_preview.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.container.BaseContainer;

/**
 * Created by iliyan on 22.11.17.
 */

public class LikesCirclesContainer extends BaseContainer<UserBase, LikeCircleViewHolder, LikesCirclesAdapter> {
    public LikesCirclesContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @NonNull
    @Override
    protected LayoutManager buildLayoutManager(@NonNull Activity activity) {
        return new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    protected LikesCirclesAdapter initializeAdapter(@NonNull Activity activity, @NonNull GlideRequests imageLoader) {
        return new LikesCirclesAdapter(activity, imageLoader);
    }
}
