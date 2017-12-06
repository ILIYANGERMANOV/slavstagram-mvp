package com.babushka.slav_squad.ui.screens.main.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.special_start.SpecialStart;
import com.babushka.slav_squad.ui.BaseActivity;
import com.babushka.slav_squad.ui.anim.AnimationEndListener;
import com.babushka.slav_squad.ui.dialog.PermissionDenyDialog;
import com.babushka.slav_squad.ui.dialog.PermissionNeverAskDialog;
import com.babushka.slav_squad.ui.dialog.PermissionRationaleDialog;
import com.babushka.slav_squad.ui.screens.about.view.AboutActivity;
import com.babushka.slav_squad.ui.screens.landing.view.LandingActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.main.model.MainModel;
import com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter;
import com.babushka.slav_squad.ui.screens.main.view.custom_view.MainPostsContainer;
import com.babushka.slav_squad.ui.screens.post_preview.view.PostPreviewActivity;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.babushka.slav_squad.ui.screens.upload_post.view.UploadPostActivity;
import com.babushka.slav_squad.util.AppUtil;
import com.babushka.slav_squad.util.IntentBuilder;
import com.google.firebase.auth.FirebaseUser;
import com.wonderkiln.blurkit.BlurLayout;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter.ACTION_POST_PREVIEW;
import static com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter.POST_ID;

@RuntimePermissions
public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View {
    public static final int SCROLL_THRESHOLD = 30;
    //TODO: Refactor and optimize post loading by moving it on another thread
    //TODO: !IMPORTANT Refactor upload post animation ugly code

    @BindView(R.id.main_toolbar)
    Toolbar vToolbar;
    @BindView(R.id.main_drawer_layout)
    DrawerLayout vDrawerLayout;
    @BindView(R.id.main_navigation_view)
    NavigationView vNavigation;
    @BindView(R.id.nav_drawer_profile_picture_circle_image_view)
    CircleImageView vNavDrawerProfileImage;
    @BindView(R.id.nav_drawer_display_name_text_view)
    TextView vNavDrawerDisplayNameText;
    @BindView(R.id.main_posts_container)
    MainPostsContainer vPostsContainer;
    @BindView(R.id.main_add_post_fab)
    FloatingActionButton vAddPostFab;
    @BindView(R.id.main_upload_post_blur_layout)
    BlurLayout vUploadPostLayout;

    @Nullable
    private MaterialDialog mProgressDialog;
    private Menu mMenu;

    private boolean mIsUploadPostLayoutShown = false;


    @NonNull
    public static SpecialStart postPreviewStart(@NonNull String postId) {
        Map<String, String> data = Collections.singletonMap(POST_ID, postId);
        return new SpecialStart(ACTION_POST_PREVIEW, data);
    }

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
        setupUploadPostLayout();
    }

    private void setupUploadPostLayout() {
        vUploadPostLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //consume all events
                return true;
            }
        });
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
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, vDrawerLayout, vToolbar,
                R.string.open_nav_drawer, R.string.close_nav_drawer);

        vDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @OnClick(R.id.nav_drawer_header_layout)
    public void onNavDrawerHeaderClick() {
        mPresenter.handleMyProfileClick();
    }

    @OnClick(R.id.nav_drawer_share_layout)
    public void onNavDrawerShareClick() {
        mPresenter.handleShareClick();
    }

    @OnClick(R.id.nav_drawer_feedback_layout)
    public void onNavDrawerFeedbackClick() {
        Toast.makeText(this, "Feedback click", Toast.LENGTH_SHORT).show();
        closeNavDrawer(true);
    }

    @OnClick(R.id.nav_drawer_about_layout)
    public void onNavDrawerAboutClick() {
        mPresenter.handleAboutClick();
    }

    @OnClick(R.id.nav_drawer_logout_layout)
    public void onNavDrawerLogoutClick() {
        closeNavDrawer(false);
        mPresenter.handleLogoutClick();
    }

    private void closeNavDrawer(boolean animate) {
        vDrawerLayout.closeDrawer(Gravity.LEFT, animate);
    }

    @OnClick(R.id.main_toolbar_slav_squad_logo_image_view)
    public void onToolbarLogoClick() {
        vPostsContainer.scrollToPosition(0);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.handleSpecialStart(SpecialStart.consumeLoaded(this));
        mPresenter.displayAllPostsInRealTime();
        mPresenter.displayUserProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        mPresenter.initMusic();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_music:
                mPresenter.toggleMusic();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected MainContract.Presenter initializePresenter() {
        SessionManager sessionManager = SessionManager.getInstance();
        FirebaseUser user = sessionManager.getCurrentFirebaseUser();
        return new MainPresenter(this, new MainModel(this, user),
                sessionManager, MyApp.getMusicPlayer());
    }

    @OnClick(R.id.main_add_post_fab)
    public void onAddPostFabClicked() {
        mPresenter.handleUploadPostFabClick();
    }

    @OnClick({R.id.main_upload_camera_image_button, R.id.main_upload_camera_button_label_text_view})
    public void onUploadPostWithCameraClick() {
        mPresenter.handleUploadPostCameraClick();
    }

    @OnClick({R.id.main_upload_gallery_image_button, R.id.main_upload_gallery_button_label_text_view})
    public void onUploadPostWithGalleryClick() {
        mPresenter.handleUploadPostGalleryClick();
    }

    @OnClick(R.id.main_upload_cancel_button)
    public void onUploadPostCancelClick() {
        mPresenter.handleUploadPostCancelClick();
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
    public void displayUserProfilePicture(@NonNull String url) {
        GlideApp.with(this)
                .load(url)
                .into(vNavDrawerProfileImage);
    }

    @Override
    public void displayUserName(@NonNull String displayName) {
        vNavDrawerDisplayNameText.setText(displayName);
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
    public void showUploadPostLayout() {
        if (AppUtil.isLollipopOrAbove()) {
            circularRevealUploadPostLayout();
        } else {
            makeUploadPostLayoutVisibleAndRefresh();
        }
        mIsUploadPostLayoutShown = true;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularRevealUploadPostLayout() {
        final Animator circularReveal = newCircularRevealAnimation();

        final Animation scaleDownFAB = new ScaleAnimation(
                1f, 0f, // Start and end values for the X axis scaling
                1f, 0f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        scaleDownFAB.setDuration(100);
        scaleDownFAB.setAnimationListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                vAddPostFab.setVisibility(View.INVISIBLE);
                circularReveal.start();
            }
        });

        vAddPostFab.startAnimation(scaleDownFAB);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private Animator newCircularRevealAnimation() {
        // get the center for the clipping circle
        int cx = (int) (vAddPostFab.getX() + vAddPostFab.getWidth() / 2);
        int cy = (int) (vAddPostFab.getY() + vAddPostFab.getHeight() / 2);

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(vUploadPostLayout.getWidth(), vUploadPostLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        final Animator circularReveal =
                ViewAnimationUtils.createCircularReveal(vUploadPostLayout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(400);
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                vUploadPostLayout.setVisibility(View.VISIBLE);
                vUploadPostLayout.invalidate();
            }
        });
        return circularReveal;
    }

    private void makeUploadPostLayoutVisibleAndRefresh() {
        vUploadPostLayout.setVisibility(View.VISIBLE);
        vUploadPostLayout.invalidate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vUploadPostLayout.invalidate();
            }
        }, 30);
    }

    @Override
    public void hideUploadPostLayout() {
        if (AppUtil.isLollipopOrAbove()) {
            circularHideUploadPostLayout();
        } else {
            vUploadPostLayout.setVisibility(View.INVISIBLE);
        }
        mIsUploadPostLayoutShown = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularHideUploadPostLayout() {
        final Animation scaleUpFAB = new ScaleAnimation(
                0f, 1f, // Start and end values for the X axis scaling
                0f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        scaleUpFAB.setDuration(100);
        scaleUpFAB.setAnimationListener(new AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                vAddPostFab.setVisibility(View.VISIBLE);
            }
        });

        Animator circularHideAnimation = newCircularHideAnimation(new Runnable() {
            @Override
            public void run() {
                vAddPostFab.startAnimation(scaleUpFAB);
            }
        });
        circularHideAnimation.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private Animator newCircularHideAnimation(final @NonNull Runnable onAnimationEnd) {
        // get the center for the clipping circle
        int cx = (int) (vAddPostFab.getX() + vAddPostFab.getWidth() / 2);
        int cy = (int) (vAddPostFab.getY() + vAddPostFab.getHeight() / 2);

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(vUploadPostLayout.getWidth(), vUploadPostLayout.getHeight());

        // create the animation (the final radius is zero)
        Animator circularHide =
                ViewAnimationUtils.createCircularReveal(vUploadPostLayout, cx, cy, initialRadius, 0);
        circularHide.setDuration(300);
        // make the view invisible when the animation is done
        circularHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                vUploadPostLayout.invalidate();
                vUploadPostLayout.setVisibility(View.INVISIBLE);
                onAnimationEnd.run();
            }
        });
        return circularHide;
    }

    @Override
    public void onBackPressed() {
        if (vDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeNavDrawer(true);
            return;
        }

        if (mIsUploadPostLayoutShown) {
            hideUploadPostLayout();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void uploadPostViaCamera(int requestCode) {
        MainActivityPermissionsDispatcher.openUploadPostCameraScreenWithCheck(this, requestCode);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void openUploadPostCameraScreen(int requestCode) {
        UploadPostActivity.startScreenForResult(this, requestCode, true);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(@NonNull PermissionRequest request) {
        new PermissionRationaleDialog(getString(R.string.permission_camera_rationale_title),
                getString(R.string.permission_camera_rationale_content), request).show(this);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showDeniedForCamera() {
        new PermissionDenyDialog(getString(R.string.permission_camera_deny_title),
                getString(R.string.permission_camera_deny_content)).show(this);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void showNeverAskForCamera() {
        new PermissionNeverAskDialog(getString(R.string.permission_camera_never_ask_title),
                getString(R.string.permission_camera_never_ask_content)).show(this);
    }

    @Override
    public void openGalleryWithCheck(int requestCode) {
        MainActivityPermissionsDispatcher.openGalleryWithCheck(this, requestCode);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void openGallery(int requestCode) {
        try {
            Intent intent = IntentBuilder.buildOpenGalleryWithChooserIntent(this);
            startActivityForResult(intent, requestCode);
        } catch (IntentBuilder.ResolveActivityException e) {
            showToast("Gallery app not found on device, please install one :)");
        }
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForReadStorage(@NonNull PermissionRequest request) {
        new PermissionRationaleDialog(getString(R.string.permission_read_storage_rationale_title),
                getString(R.string.permission_read_storage_rationale_content), request).show(this);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showDeniedForReadStorage() {
        new PermissionDenyDialog(getString(R.string.permission_read_storage_deny_title),
                getString(R.string.permission_read_storage_deny_content)).show(this);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showNeverAskForReadStorage() {
        new PermissionNeverAskDialog(getString(R.string.permission_read_storage_never_ask_title),
                getString(R.string.permission_read_storage_never_ask_content)).show(this);
    }

    @Override
    public void openUploadPostScreen(int requestCode, @NonNull Uri selectedImage) {
        UploadPostActivity.startScreenForResult(this, requestCode, selectedImage);
    }

    @Override
    public void openPostPreview(@NonNull Post post) {
        PostPreviewActivity.startScreen(this, post);
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
    public void openAboutScreen() {
        AboutActivity.startScreen(this);
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
    public void showMusicPlaying() {
        setMusicMenuIcon(R.drawable.ic_volume_on);
    }

    @Override
    public void showMusicStopped() {
        setMusicMenuIcon(R.drawable.ic_volume_off);
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setMusicMenuIcon(@DrawableRes int menuIcon) {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_toggle_music);
            item.setIcon(menuIcon);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialogIfShown();
    }
}
