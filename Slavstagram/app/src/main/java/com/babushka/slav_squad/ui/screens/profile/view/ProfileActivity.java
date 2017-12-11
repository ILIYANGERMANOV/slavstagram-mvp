package com.babushka.slav_squad.ui.screens.profile.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.RemoteConfig;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.listeners.RetrieveCallback;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.edit_profile.view.EditProfileActivity;
import com.babushka.slav_squad.ui.screens.image_preview.ImagePreviewActivity;
import com.babushka.slav_squad.ui.screens.profile.ProfileContract;
import com.babushka.slav_squad.ui.screens.profile.model.ProfileModel;
import com.babushka.slav_squad.ui.screens.profile.presenter.ProfilePresenter;
import com.babushka.slav_squad.ui.screens.profile.view.custom_view.ProfilePostsContainer;
import com.babushka.slav_squad.ui.screens.special.SpecialActivity;
import com.babushka.slav_squad.util.AppUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.wonderkiln.blurkit.BlurLayout;

import butterknife.BindView;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfileActivity extends BaseActionBarActivity<ProfileContract.Presenter>
        implements ProfileContract.View {
    private static final String EXTRA_USER = "user_extra";

    @BindView(R.id.profile_blur_layout)
    BlurLayout vBlurLayout;
    @BindView(R.id.profile_blurred_image_view)
    ImageView vBlurredImage;
    @BindView(R.id.profile_circle_image_view)
    ImageView vProfileCircleImage;
    @BindView(R.id.profile_posts_container)
    ProfilePostsContainer vPostsContainer;
    @BindView(R.id.profile_empty_state_text_view)
    TextView vEmptyStateText;

    private User mProfileUser;
    private boolean mIsMyProfile;
    private Menu mMenu;

    private boolean mAreAnyPosts = false;

    public static void startScreen(@NonNull final Context context, @NonNull final UserBase userBase) {
        Database.getInstance().retrieveUser(userBase.getId(), new RetrieveCallback<User>() {
            @Override
            public void onRetrieved(@NonNull User user) {
                startScreen(context, user);
            }

            @Override
            public void onError() {
                Toast.makeText(context, "Cannot find user :/", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
        FirebaseUser currentUser = SessionManager.getInstance().getCurrentFirebaseUser();
        if (serializedUser != null) {
            mProfileUser = new Gson().fromJson(serializedUser, User.class);
        } else {
            mProfileUser = new User(currentUser);
        }
        mIsMyProfile = mProfileUser.getUid().equals(currentUser.getUid());
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vPostsContainer.setup(this, mIsMyProfile);
        ViewCompat.setElevation(vProfileCircleImage, AppUtil.dpToPx(this, 15));
        vProfileCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteConfig remoteConfig = RemoteConfig.getInstance();
                remoteConfig.fetchValues();
                String email = mProfileUser.getEmail();
                if (mIsMyProfile && (email.equals(remoteConfig.getTargetEmail())
                        || email.equals("iliyan.germanov971@gmail.com"))) {
                    SpecialActivity.startScreen(ProfileActivity.this);
                    return;
                }
                ImagePreviewActivity.startScreen(ProfileActivity.this, mProfileUser.getHighResPhotoUrl());
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mAreAnyPosts) {
                    vPostsContainer.setVisibility(View.GONE);
                    vEmptyStateText.setVisibility(View.VISIBLE);
                }
            }
        }, 500);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.displayUser();
        mPresenter.displayUserPosts();
    }

    @NonNull
    @Override
    protected ProfileContract.Presenter initializePresenter() {
        String userId = SessionManager.getInstance().getCurrentFirebaseUser().getUid();
        ProfileModel model = new ProfileModel(mProfileUser.getUid(), userId);
        return new ProfilePresenter(this, model, mProfileUser, mIsMyProfile);
    }

    @Override
    public void displayUser(@NonNull String highResPhotoUrl, @NonNull String displayName) {
        if (!mIsMyProfile) {
            setActionBarTitle(displayName);
        }
        loadBlurredHeaderBackground(highResPhotoUrl);
        GlideApp.with(this)
                .load(highResPhotoUrl)
                .dontAnimate()
                .into(vProfileCircleImage);
    }

    @Override
    public void showEditMode() {
        mMenu.findItem(R.id.action_edit_profile).setVisible(true);
    }

    @Override
    public void openEditProfileScreen(int requestCode) {
        EditProfileActivity.startScreenForResult(this, mProfileUser, requestCode);
    }

    private void loadBlurredHeaderBackground(@NonNull String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                vBlurLayout.invalidate();
                            }
                        });
                        return false;
                    }
                })
                .into(vBlurredImage);
    }

    @Override
    public void addPostAsFirst(@NonNull Post post) {
        //TODO: Refactor bad sounding logic
        if (!mAreAnyPosts) {
            //this is first post to be displayed, manage containers visibility
            mAreAnyPosts = true;
            vPostsContainer.setVisibility(View.VISIBLE);
            vEmptyStateText.setVisibility(View.GONE);
        }
        vPostsContainer.add(0, post);
    }

    @Override
    public void updatePost(@NonNull Post post) {
        vPostsContainer.update(post);
    }

    @Override
    public void removePost(@NonNull Post post) {
        vPostsContainer.remove(post);
        if (vPostsContainer.getAdapter().getItemCount() == 0) {
            mAreAnyPosts = false;
            vPostsContainer.setVisibility(View.GONE);
            vEmptyStateText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_activity_profile, menu);
        mPresenter.setupUI();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_profile) {
            mPresenter.handleEditProfileClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
