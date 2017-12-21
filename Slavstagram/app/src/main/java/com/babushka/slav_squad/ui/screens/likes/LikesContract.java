package com.babushka.slav_squad.ui.screens.likes;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.DatabaseListener;
import com.babushka.slav_squad.persistence.database.listeners.RetrieveCallback;
import com.babushka.slav_squad.persistence.database.listeners.ValueListener;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.BasePresenter;

import java.util.List;

/**
 * Created by iliyan on 09.11.17.
 */

public interface LikesContract {
    interface View {
        void displayLikesCount(int likesCount);

        void showToast(@NonNull String message);

        void onLikeAdded(@NonNull UserBase user);

        void onLikeChanged(@NonNull UserBase user);

        void onLikeRemoved(@NonNull UserBase user);
    }

    interface Presenter extends BasePresenter {
        void displayLikes();
    }

    interface Model {
        void retrieveLikes(@NonNull String postId, @NonNull RetrieveCallback<List<UserBase>> callback);

        void addLikesCountListener(@NonNull String postId, @NonNull ValueListener<Integer> listener);

        void addLikesListener(@NonNull String postId, @NonNull DatabaseListener<UserBase> listener);

        void removeLikesListener(@NonNull String postId);

        void removeLikesCountListener(@NonNull String postId);
    }
}
