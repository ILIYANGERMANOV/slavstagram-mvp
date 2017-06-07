package com.babushka.slav_squad.ui.screens.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.BaseAdapter;

/**
 * Created by iliyan on 29.05.17.
 */

public abstract class BasePostsAdapter extends BaseAdapter<Post, PostViewHolder> {
    @NonNull
    private final GlideRequests mImageLoader;

    public BasePostsAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader) {
        super(context);
        mImageLoader = imageLoader;
    }

    @Override
    protected PostViewHolder initializeViewHolder(@NonNull View layoutView) {
        return new PostViewHolder(layoutView, mImageLoader);
    }
}
