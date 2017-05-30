package com.babushka.slav_squad.ui.screens.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.BaseActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter;
import com.babushka.slav_squad.ui.screens.main.view.custom_view.PostsContainer;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.google.firebase.database.DatabaseError;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter>
        implements MainContract.View {
    @BindView(R.id.main_posts_container)
    PostsContainer mPostsContainer;

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
        mPostsContainer.setup(this, GlideApp.with(this));
    }

    @Override
    protected void onSetupFinished() {
        //TODO: Refactor to MVP
        Database.getInstance().addPostsListener(new PostsListener() {
            @Override
            public void onPostAdded(@NonNull Post post) {
                //add post as first
                mPostsContainer.add(0, post);
            }

            @Override
            public void onPostChanged(@NonNull Post post) {

            }

            @Override
            public void onPostRemoved(@NonNull Post post) {

            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    protected MainPresenter initializePresenter() {
        return new MainPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_post:
                User author = new User(SessionManager.getInstance().getCurrentUser());
                String imageUrl = "http://i.imgur.com/LmS9Fci.jpg";
                Post.Image image = new Post.Image(imageUrl, 600, 400);
                Post post = new Post(author, "Slavformers", image);
                Database.getInstance().saveNewPost(post);
                return true;
            case R.id.action_logout:
                SessionManager.getInstance().logout();
                SplashActivity.startScreenAsEntryPoint(this);
                return true;
        }
        return false;
    }
}
