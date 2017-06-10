package com.babushka.slav_squad.persistence.database.listeners;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Comment;

/**
 * Created by iliyan on 10.06.17.
 */

public interface CommentsListener extends DatabaseErrorCallback {
    void onCommentAdded(@NonNull Comment comment);

    void onCommentChanged(@NonNull Comment comment);

    void onCommentRemoved(@NonNull Comment comment);
}
