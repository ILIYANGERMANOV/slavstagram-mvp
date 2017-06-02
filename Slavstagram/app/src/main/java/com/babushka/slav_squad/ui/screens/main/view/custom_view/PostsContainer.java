package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.BaseContainer;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by iliyan on 29.05.17.
 */

public class PostsContainer extends BaseContainer<Post, PostViewHolder, PostsAdapter> {
    public PostsContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected PostsAdapter initializeAdapter(@NonNull Activity activity, @NonNull GlideRequests imageLoader) {
        return new PostsAdapter(activity, imageLoader);
    }

    @Override
    protected void onCustomSetup(@NonNull Activity activity) {
        addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity)
                .color(ContextCompat.getColor(activity, R.color.posts_divider_color))
                .marginResId(R.dimen.activity_horizontal_margin_half)
                .sizeResId(R.dimen.posts_divider_height)
                .build());
    }
}
