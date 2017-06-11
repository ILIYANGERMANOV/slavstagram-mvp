package com.babushka.slav_squad.ui.screens.comments.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.listeners.CommentsListener;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.comments.CommentsContract;

/**
 * Created by iliyan on 10.06.17.
 */

public class CommentsModel implements CommentsContract.Model {
    @NonNull
    private final Post mPost;

    public CommentsModel(@NonNull Post post) {
        mPost = post;
    }

    @Override
    public void addCommentsListener(@NonNull CommentsListener commentsListener) {
        Database.getInstance().addCommentsListener(mPost, commentsListener);
    }

    @Override
    public void removeCommentsListener() {
        Database.getInstance().removeCommentsEventListener(mPost.getId());
    }

    @Override
    public void addComment(@NonNull Comment comment) {
        Database.getInstance().addComment(mPost, comment);
    }
}
