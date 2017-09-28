package com.babushka.slav_squad.ui.screens.preview_post;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.comments.CommentsContract;

/**
 * Created by iliyan on 26.09.17.
 */

public interface PreviewPostContract {
    interface View extends CommentsContract.View {
        void displayPostImage(@NonNull Post.Image image);

        void displayPostLikes(int likesCount);

        void displayDescription(@NonNull String description);
    }

    interface Presenter extends CommentsContract.Presenter {

    }

    interface Model extends CommentsContract.Model {

    }
}
