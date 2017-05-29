package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.BaseContainer;

/**
 * Created by iliyan on 29.05.17.
 */

public class PostsContainer extends BaseContainer<Post, PostViewHolder, PostsAdapter> {
    public PostsContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected PostsAdapter initializeAdapter(@NonNull Activity activity) {
        return new PostsAdapter(activity);
    }
}
