package com.babushka.slav_squad.ui.screens.profile.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.GlideAdapter;

/**
 * Created by iliyan on 07.06.17.
 */

class ProfilePostsAdapter extends GlideAdapter<Post, ProfilePostViewHolder> {
    private final boolean mIsMyProfile;

    ProfilePostsAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader,
                        boolean isMyProfile) {
        super(context, imageLoader);
        mIsMyProfile = isMyProfile;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_profile_post_view_holder;
    }

    int calculateSpanSize(int position) {
        Post.Image image = mData.get(position).getImage();
        return image.getWidth() > image.getHeight() ? 2 : 1;
    }

    @Override
    protected ProfilePostViewHolder initializeViewHolder(@NonNull View layoutView) {
        return new ProfilePostViewHolder(layoutView, mImageLoader, mIsMyProfile);
    }
}
