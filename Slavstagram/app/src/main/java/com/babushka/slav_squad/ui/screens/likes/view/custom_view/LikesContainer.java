package com.babushka.slav_squad.ui.screens.likes.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.container.BaseContainer;

/**
 * Created by iliyan on 09.11.17.
 */

public class LikesContainer extends BaseContainer<UserBase, LikeViewHolder, LikesAdapter> {
    public LikesContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected LikesAdapter initializeAdapter(@NonNull Activity activity, @NonNull GlideRequests imageLoader) {
        return new LikesAdapter(activity, imageLoader);
    }
}
