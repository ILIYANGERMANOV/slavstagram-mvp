package com.babushka.slav_squad.ui.screens.main.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActivity;
import com.babushka.slav_squad.ui.dialog.PermissionDenyDialog;
import com.babushka.slav_squad.ui.dialog.PermissionNeverAskDialog;
import com.babushka.slav_squad.ui.dialog.PermissionRationaleDialog;
import com.babushka.slav_squad.ui.screens.landing.landing.view.LandingActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.main.model.MainModel;
import com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter;
import com.babushka.slav_squad.ui.screens.main.view.custom_view.MainPostsContainer;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.babushka.slav_squad.ui.screens.upload_post.view.UploadPostActivity;
import com.babushka.slav_squad.util.IntentBuilder;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View {
    public static final int SCROLL_THRESHOLD = 30;
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

    @Nullable
    private MaterialDialog mProgressDialog;

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
                if (dy > SCROLL_THRESHOLD && isShown) {
                    vAddPostFab.hide();
                } else if (dy < -SCROLL_THRESHOLD && !isShown) {
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
                switch (item.getItemId()) {
                    case R.id.nav_drawer_action_profile:
                        mPresenter.handleMyProfileClick();
                        vDrawerLayout.closeDrawer(Gravity.LEFT, false);
                        break;
                    case R.id.nav_drawer_action_share:
                        mPresenter.handleShareClick();
                        vDrawerLayout.closeDrawer(Gravity.LEFT, false);
                        break;
                    case R.id.nav_drawer_action_log_out:
                        mPresenter.handleLogoutClick();
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
        return new MainPresenter(this, new MainModel(this, user));
    }

    @OnClick(R.id.main_add_post_fab)
    public void onAddPostFabClicked() {
        mPresenter.handleUploadPostClick();
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

    @Override
    public void downloadPostWithPermissionCheck(@NonNull String imageUrl) {
        MainActivityPermissionsDispatcher.downloadPostWithCheck(this, imageUrl);
    }

    @Override
    public void showDownloadPostLoading() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title("Downloading post")
                .content("Will take a moment :)")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @Override
    public void showDownloadPostSuccess() {
        dismissProgressDialogIfShown();
        Toast.makeText(this, "Post downloaded successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDownloadPostError() {
        dismissProgressDialogIfShown();
        Toast.makeText(this, "Error while downloading post", Toast.LENGTH_SHORT).show();
    }

    private void dismissProgressDialogIfShown() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void downloadPost(@NonNull String imageUrl) {
        mPresenter.downloadPost(imageUrl);
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForWriteStorage(@NonNull PermissionRequest request) {
        new PermissionRationaleDialog(getString(R.string.permission_write_storage_rationale_title),
                getString(R.string.permission_write_storage_rationale_content), request).show(this);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showDeniedForWriteStorage() {
        new PermissionDenyDialog(getString(R.string.permission_write_storage_deny_title),
                getString(R.string.permission_write_storage_deny_content)).show(this);

    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showNeverAskForWriteStorage() {
        new PermissionNeverAskDialog(getString(R.string.permission_write_storage_never_ask_title),
                getString(R.string.permission_write_storage_never_ask_content)).show(this);
    }


    @Override
    public void addImageToGallery(@NonNull File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    public void openUploadPostScreen() {
        UploadPostActivity.startScreen(this);
    }

    @Override
    public void promptGuestToLogin() {
        LandingActivity.startScreen(this);
    }

    @Override
    public void openProfileScreen() {
        ProfileActivity.startScreen(MainActivity.this);
    }

    @Override
    public void fireShareIntent(@NonNull String textToShare) {
        Intent shareIntent = IntentBuilder.buildShareIntent(textToShare);
        startActivity(shareIntent);
    }

    @Override
    public void startSplashScreenAsEntry() {
        SplashActivity.startScreenAsEntryPoint(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialogIfShown();
    }
}
