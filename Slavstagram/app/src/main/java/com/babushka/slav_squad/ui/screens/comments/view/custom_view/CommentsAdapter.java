package com.babushka.slav_squad.ui.screens.comments.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.GlideAdapter;

/**
 * Created by iliyan on 11.06.17.
 */

public class CommentsAdapter extends GlideAdapter<Comment, CommentViewHolder> {
    @NonNull
    private final Post mPost;

    protected CommentsAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader,
                              @NonNull Post post) {
        super(context, imageLoader);
        mPost = post;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_comment_view_holder;
    }

    @Override
    protected CommentViewHolder initializeViewHolder(@NonNull View layoutView) {
        return new CommentViewHolder(layoutView, mImageLoader, mPost);
    }
}
