package com.babushka.slav_squad.ui.screens.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.main.model.MainModel;
import com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter;
import com.babushka.slav_squad.ui.screens.main.view.custom_view.MainPostsContainer;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.babushka.slav_squad.ui.screens.upload_post.view.UploadPostActivity;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View {
    public static final int SCROLL_SENSITIVITY = 30;
    //TODO: Refactor and optimize post loading by moving it on another thread

    @BindView(R.id.main_drawer_layout)
    DrawerLayout vDrawerLayout;
    @BindView(R.id.main_navigation_view)
    NavigationView vNavigation;
    @BindView(R.id.main_toolbar)
    Toolbar vToolbar;
    @BindView(R.id.main_posts_container)
    MainPostsContainer vPostsContainer;
    @BindView(R.id.main_add_post_fab)
    FloatingActionButton vAddPostFab;

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
        setupPostsContainer();
        setupToolbar();
        setupNavDrawer();
    }

    private void setupPostsContainer() {
        vPostsContainer.setup(this);
        vPostsContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean isShown = vAddPostFab.isShown();
                if (dy > SCROLL_SENSITIVITY && isShown) {
                    vAddPostFab.hide();
                } else if (dy < -SCROLL_SENSITIVITY && !isShown) {
                    vAddPostFab.show();
                }
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(vToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setupNavDrawer() {
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //TODO: Migrate to MVP
                switch (item.getItemId()) {
                    case R.id.nav_drawer_action_profile:
                        ProfileActivity.startScreen(MainActivity.this);
                        vDrawerLayout.closeDrawer(Gravity.LEFT, false);
                        break;
                    case R.id.nav_drawer_action_log_out:
                        SessionManager.getInstance().logout();
                        SplashActivity.startScreenAsEntryPoint(MainActivity.this);
                        break;
                }
                return false;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, vDrawerLayout, vToolbar,
                R.string.open_nav_drawer, R.string.close_nav_drawer);

        vDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.displayAllPostsInRealTime();
    }

    @NonNull
    @Override
    protected MainContract.Presenter initializePresenter() {
        FirebaseUser user = SessionManager.getInstance().getCurrentFirebaseUser();
        return new MainPresenter(this, new MainModel(user));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_activity_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return false;
//    }

    @OnClick(R.id.main_add_post_fab)
    public void onAddPostFabClicked() {
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
