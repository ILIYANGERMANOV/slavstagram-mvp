package com.babushka.slav_squad.ui.screens.post_container;

import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.BaseAdapter;

import butterknife.BindView;

/**
 * Created by iliyan on 29.05.17.
 */

public class PostViewHolder extends BaseAdapter.BaseViewHolder<Post> {
    @NonNull
    private final GlideRequests mImageLoader;
    @BindView(R.id.adapter_post_post_view)
    protected PostView mPostView;

    protected PostViewHolder(View itemView, @NonNull GlideRequests imageLoader) {
        super(itemView);
        mImageLoader = imageLoader;
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
