package com.babushka.slav_squad.ui.screens.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.main.model.MainModel;
import com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter;
import com.babushka.slav_squad.ui.screens.main.view.custom_view.PostsContainer;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.babushka.slav_squad.ui.screens.upload_post.view.UploadPostActivity;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View {
    @BindView(R.id.main_posts_container)
    PostsContainer vPostsContainer;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSetupUI() {
        vPostsContainer.setup(this, GlideApp.with(this));
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.displayAllPostsInRealtime();
    }

    @NonNull
    @Override
    protected MainContract.Presenter initializePresenter() {
        FirebaseUser user = SessionManager.getInstance().getCurrentUser();
        return new MainPresenter(this, new MainModel(user));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: Migrate to MVP
        switch (item.getItemId()) {
            case R.id.action_profile:
                ProfileActivity.startScreen(this);
                break;
            case R.id.action_logout:
                SessionManager.getInstance().logout();
                SplashActivity.startScreenAsEntryPoint(this);
                return true;
        }
        return false;
    }

    @OnClick(R.id.main_add_post_fab)
    public void onAddPostClicked() {
        UploadPostActivity.startScreen(this);
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
