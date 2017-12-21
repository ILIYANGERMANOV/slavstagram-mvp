package com.babushka.slav_squad.ui.screens.post_preview;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.comments.CommentsContract;

/**
 * Created by iliyan on 26.09.17.
 */

public interface PostPreviewContract {
    interface View extends CommentsContract.View {
        void displayPostImage(@NonNull Post.Image image);

        void displayDescription(@NonNull String description);

        void showToast(@NonNull String message);
    }

    interface Presenter extends CommentsContract.Presenter {

    }

    interface Model extends CommentsContract.Model {

    }
}
