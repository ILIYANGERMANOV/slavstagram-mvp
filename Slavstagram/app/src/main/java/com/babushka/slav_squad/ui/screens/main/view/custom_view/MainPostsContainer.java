package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.ui.screens.post.BasePostsContainer;

/**
 * Created by iliyan on 29.05.17.
 */

public class MainPostsContainer extends BasePostsContainer<MainPostsAdapter> {
    public MainPostsContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected MainPostsAdapter initializeAdapter(@NonNull Activity activity, @NonNull GlideRequests imageLoader) {
        return new MainPostsAdapter(activity, imageLoader);
    }
}
