package com.babushka.slav_squad.ui.screens.profile.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.ui.screens.post_container.BasePostsContainer;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfilePostsContainer extends BasePostsContainer<ProfilePostsAdapter, ProfilePostViewHolder> {
    private boolean mIsMyProfile;

    public ProfilePostsContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setNestedScrollingEnabled(false);
    }

    @Override
    protected ProfilePostsAdapter initializeAdapter(@NonNull Activity activity,
                                                    @NonNull GlideRequests imageLoader) {
        return new ProfilePostsAdapter(activity, imageLoader, mIsMyProfile);
    }

    public void setup(@NonNull Activity activity, boolean isMyProfile) {
        mIsMyProfile = isMyProfile;
        setup(activity);
    }

    @NonNull
    @Override
    protected LayoutManager buildLayoutManager(@NonNull Activity activity) {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.calculateSpanSize(position);
            }
        });
        return layoutManager;
    }
}
