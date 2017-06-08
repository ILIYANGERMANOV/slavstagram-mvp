package com.babushka.slav_squad.ui.screens.profile.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.profile.ProfileContract;
import com.babushka.slav_squad.ui.screens.profile.model.ProfileModel;
import com.babushka.slav_squad.ui.screens.profile.presenter.ProfilePresenter;
import com.babushka.slav_squad.ui.screens.profile.view.custom_view.ProfilePostsContainer;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfileActivity extends BaseActionBarActivity<ProfileContract.Presenter>
        implements ProfileContract.View {
    private static final String EXTRA_USER = "user_extra";

    @BindView(R.id.profile_posts_container)
    ProfilePostsContainer vPostsContainer;

    private User mUser;

    public static void startScreen(@NonNull Context context, @NonNull User user) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_USER, new Gson().toJson(user));
        context.startActivity(intent);
    }

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        String serializedUser = intent.getStringExtra(EXTRA_USER);
        if (serializedUser != null) {
            mUser = new Gson().fromJson(serializedUser, User.class);
        } else {
            FirebaseUser currentUser = SessionManager.getInstance().getCurrentUser();
            mUser = new User(currentUser);
        }
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vPostsContainer.setup(this);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.displayUserAndLoadHisPosts();
    }

    @NonNull
    @Override
    protected ProfileContract.Presenter initializePresenter() {
        return new ProfilePresenter(this, new ProfileModel(mUser.getUid()), mUser);
    }

    @Override
    public void displayUser(@NonNull String imageUrl, @NonNull String displayName) {
        setActionBarTitle(displayName);
    }

    @Override
    public void addPostAsFirst(@NonNull Post post) {
        vPostsContainer.add(0, post);
    }

    @Override
    public void updatePost(@NonNull Post post) {
        vPostsContainer.update(post);
    }

    @Override
    public void removePost(@NonNull Post post) {
        vPostsContainer.remove(post);
    }
}
