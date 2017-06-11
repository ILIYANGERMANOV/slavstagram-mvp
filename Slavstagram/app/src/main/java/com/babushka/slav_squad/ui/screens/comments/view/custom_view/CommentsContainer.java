package com.babushka.slav_squad.ui.screens.comments.view.custom_view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.ui.container.BaseContainer;

/**
 * Created by iliyan on 11.06.17.
 */

public class CommentsContainer extends BaseContainer<Comment, CommentViewHolder, CommentsAdapter> {
    public CommentsContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setNestedScrollingEnabled(false);
    }

    @Override
    protected CommentsAdapter initializeAdapter(@NonNull Activity activity, @NonNull GlideRequests imageLoader) {
        return new CommentsAdapter(activity, imageLoader);
    }
}
