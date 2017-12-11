package com.babushka.slav_squad.ui.screens.profile.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.listeners.DatabaseListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.BasePostsModel;
import com.babushka.slav_squad.ui.screens.profile.ProfileContract;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfileModel extends BasePostsModel implements ProfileContract.Model {
    @NonNull
    private final String mProfileId;

    public ProfileModel(@NonNull String profileId, @NonNull String userId) {
        super(userId);
        mProfileId = profileId;
    }

    @Override
    public void addUserPostsListener(@NonNull DatabaseListener<Post> postsListener) {
        Database database = Database.getInstance();
        database.addUserPostsListener(mProfileId, buildIsLikedPostTransformator(postsListener));
    }

    @Override
    public void removeUserPostsListener() {
        Database.getInstance().removeUserPostsListener(mProfileId);
    }
}
