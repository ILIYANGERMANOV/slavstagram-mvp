package com.babushka.slav_squad.ui.screens.likes.view;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.listeners.DatabaseListener;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;
import com.google.firebase.database.DatabaseError;

/**
 * Created by iliyan on 10.12.17.
 */

public class DisplayLikesRealtimeListener implements DatabaseListener<UserBase> {
    private LikesContract.View mView;

    public DisplayLikesRealtimeListener(@NonNull LikesContract.View view) {
        mView = view;
    }

    @Override
    public void onError(@NonNull DatabaseError databaseError) {
        if (mView != null) {
            mView.showToast("Error while loading likes: " + databaseError.getMessage());
        }
    }

    @Override
    public void onAdded(@NonNull UserBase data) {
        if (mView != null) {
            mView.onLikeAdded(data);
        }
    }

    @Override
    public void onChanged(@NonNull UserBase data) {
        if (mView != null) {
            mView.onLikeChanged(data);
        }
    }

    @Override
    public void onRemoved(@NonNull UserBase data) {
        if (mView != null) {
            mView.onLikeRemoved(data);
        }
    }

    public void destroy() {
        mView = null;
    }
}
