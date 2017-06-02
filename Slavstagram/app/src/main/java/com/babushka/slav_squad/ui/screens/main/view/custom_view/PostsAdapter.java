package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.BaseAdapter;

/**
 * Created by iliyan on 29.05.17.
 */

class PostsAdapter extends BaseAdapter<Post, PostViewHolder> {
    @NonNull
    private final GlideRequests mImageLoader;

    PostsAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader) {
        super(context);
        mImageLoader = imageLoader;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_post_view_holder;
    }

    @Override
    protected PostViewHolder initializeViewHolder(@NonNull View layoutView) {
        return new PostViewHolder(layoutView, mImageLoader);
    }
}
