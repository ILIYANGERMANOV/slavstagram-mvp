package com.babushka.slav_squad.ui.screens.post_preview.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.comments.model.CommentsModel;
import com.babushka.slav_squad.ui.screens.post_preview.PostPreviewContract;

/**
 * Created by iliyan on 26.09.17.
 */

public class PostPreviewModel extends CommentsModel
        implements PostPreviewContract.Model {

    public PostPreviewModel(@NonNull Post post) {
        super(post);
    }
}