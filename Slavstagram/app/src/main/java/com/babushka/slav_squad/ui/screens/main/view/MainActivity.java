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
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.BaseActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.main.model.MainModel;
import com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter;
import com.babushka.slav_squad.ui.screens.main.view.custom_view.PostsContainer;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;

import java.util.Random;

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
        mPresenter.displayAllPostsInRealtime();
    }

    @NonNull
    @Override
    protected MainPresenter initializePresenter() {
        return new MainPresenter(this, new MainModel());
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
                Random random = new Random();
                Post post = buildPost1(author);
                switch (random.nextInt(3)) {
                    case 0:
                        post = buildPost1(author);
                        break;
                    case 1:
                        post = buildPost2(author);
                        break;
                    case 2:
                        post = buildPost3(author);
                        break;
                }
                Database.getInstance().saveNewPost(post);
                return true;
            case R.id.action_logout:
                SessionManager.getInstance().logout();
                SplashActivity.startScreenAsEntryPoint(this);
                return true;
        }
        return false;
    }

    @NonNull
    private Post buildPost1(User author) {
        String imageUrl = "http://i.imgur.com/LmS9Fci.jpg";
        Post.Image image = new Post.Image(imageUrl, 600, 400);
        return new Post(author, "Slavformers", image);
    }

    @NonNull
    private Post buildPost2(User author) {
        String imageUrl = "http://www.speakerscorner.me/wp-content/uploads/2017/05/Galina_Dub_9.jpg";
        Post.Image image = new Post.Image(imageUrl, 1000, 600);
        return new Post(author, "When she hears your got kompot", image);
    }

    @NonNull
    private Post buildPost3(User author) {
        String imageUrl = "http://cdn-9chat-fun.9cache.com/media/photo/aoXY4GW61_480w_v1.jpg";
        Post.Image image = new Post.Image(imageUrl, 480, 359);
        return new Post(author, "Squatting gopnica", image);
    }

    @Override
    public void addPostAsFirst(@NonNull Post post) {
        mPostsContainer.add(0, post);
    }

    @Override
    public void updatePost(@NonNull Post post) {
        mPostsContainer.update(post);
    }

    @Override
    public void removePost(@NonNull Post post) {
        mPostsContainer.remove(post);
    }
}
