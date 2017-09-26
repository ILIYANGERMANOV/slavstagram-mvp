package com.babushka.slav_squad.ui.screens.preview_post.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.comments.model.CommentsModel;
import com.babushka.slav_squad.ui.screens.preview_post.PreviewPostContract;

/**
 * Created by iliyan on 26.09.17.
 */

public class PreviewPostModel extends CommentsModel
        implements PreviewPostContract.Model {

    public PreviewPostModel(@NonNull Post post) {
        super(post);
    }
}