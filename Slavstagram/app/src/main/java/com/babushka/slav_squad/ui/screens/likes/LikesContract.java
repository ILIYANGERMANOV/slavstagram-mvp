package com.babushka.slav_squad.ui.screens.likes;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.BasePresenter;

import java.util.List;

/**
 * Created by iliyan on 09.11.17.
 */

public interface LikesContract {
    interface View {
        void displayLikes(@NonNull List<User> usersLiked);
    }

    interface Presenter extends BasePresenter {
        void displayLikes();
    }
}
