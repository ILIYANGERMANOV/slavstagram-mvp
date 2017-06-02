package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.BaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iliyan on 29.05.17.
 */

class PostViewHolder extends BaseAdapter.BaseViewHolder<Post> {
    @NonNull
    private final GlideRequests mImageLoader;
    @BindView(R.id.adapter_post_post_view)
    PostView mPostView;

    PostViewHolder(View itemView, @NonNull GlideRequests imageLoader) {
        super(itemView);
        mImageLoader = imageLoader;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void display(@NonNull Post post) {
        mPostView.display(post, mImageLoader);
    }

    @Override
    public void resetState() {
        mPostView.reset();
    }
}
