package com.babushka.slav_squad.ui.screens.profile.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.profile.ProfileContract;
import com.babushka.slav_squad.ui.screens.profile.model.ProfileModel;
import com.babushka.slav_squad.ui.screens.profile.presenter.ProfilePresenter;
import com.babushka.slav_squad.ui.screens.profile.view.custom_view.ProfilePostsContainer;
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
    @BindView(R.id.profile_description_text_view)
    TextView vDescText;
    @BindView(R.id.profile_description_edit_image_button)
    ImageButton vDescEditButton;
    @BindView(R.id.profile_posts_container)
    ProfilePostsContainer vPostsContainer;

    private User mProfileUser;
    private boolean mIsMyProfile;

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
    public void displayUser(@NonNull String imageUrl, @NonNull String displayName) {
        if (!mIsMyProfile) {
            setActionBarTitle(displayName);
        }
        loadBlurredHeaderBackground(imageUrl);
        GlideApp.with(this)
                .load(imageUrl)
                .into(vProfileCircleImage);
    }

    @Override
    public void showEditMode() {
        vDescEditButton.setVisibility(View.VISIBLE);
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
