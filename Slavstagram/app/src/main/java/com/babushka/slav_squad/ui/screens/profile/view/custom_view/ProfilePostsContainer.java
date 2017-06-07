package com.babushka.slav_squad.ui.screens.profile.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.ui.screens.post.BasePostsContainer;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfilePostsContainer extends BasePostsContainer<ProfilePostsAdapter> {
    public ProfilePostsContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ProfilePostsAdapter initializeAdapter(@NonNull Activity activity,
                                                    @NonNull GlideRequests imageLoader) {
        return new ProfilePostsAdapter(activity, imageLoader);
    }
}
