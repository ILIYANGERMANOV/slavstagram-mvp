package com.babushka.slav_squad.ui.screens.comments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.listeners.CommentsListener;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.BaseView;

/**
 * Created by iliyan on 10.06.17.
 */

public interface CommentsContract {
    interface View extends BaseView {
        void addComment(@NonNull Comment comment);

        void updateComment(@NonNull Comment comment);

        void removeComment(@NonNull Comment comment);

        void displayAuthorImage(@Nullable String imageUrl);
    }

    interface Presenter extends BasePresenter {
        void displayCurrentUser();

        void displayCommentsInRealTime();

        void addComment(@NonNull Comment comment);
    }

    interface Model {
        void addCommentsListener(@NonNull CommentsListener commentsListener);

        void removeCommentsListener();

        void addComment(@NonNull Comment comment);
    }
}
