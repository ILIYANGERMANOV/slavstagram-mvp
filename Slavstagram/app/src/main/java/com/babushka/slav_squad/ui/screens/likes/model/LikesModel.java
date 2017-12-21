package com.babushka.slav_squad.ui.screens.likes.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.listeners.DatabaseListener;
import com.babushka.slav_squad.persistence.database.listeners.RetrieveCallback;
import com.babushka.slav_squad.persistence.database.listeners.ValueListener;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;

import java.util.List;

/**
 * Created by iliyan on 10.12.17.
 */

public class LikesModel implements LikesContract.Model {
    @Override
    public void retrieveLikes(@NonNull String postId, @NonNull RetrieveCallback<List<UserBase>> callback) {
        Database.getInstance().retrieveLikes(postId, callback);
    }

    @Override
    public void addLikesCountListener(@NonNull String postId, @NonNull ValueListener<Integer> listener) {
        Database.getInstance().addLikesCountListener(postId, listener);
    }

    @Override
    public void addLikesListener(@NonNull String postId, @NonNull DatabaseListener<UserBase> listener) {
        Database.getInstance().addLikesListener(postId, listener);
    }

    @Override
    public void removeLikesListener(@NonNull String postId) {
        Database.getInstance().removeLikesListener(postId);
    }

    @Override
    public void removeLikesCountListener(@NonNull String postId) {
        Database.getInstance().removeLikesCountListener(postId);
    }
}
