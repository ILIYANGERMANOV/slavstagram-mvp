package com.babushka.slav_squad.ui.screens.post_preview.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.custom_view.AspectRatioPhotoView;
import com.babushka.slav_squad.ui.custom_view.LockableNestedScrollView;
import com.babushka.slav_squad.ui.dialog.PermissionDenyDialog;
import com.babushka.slav_squad.ui.dialog.PermissionNeverAskDialog;
import com.babushka.slav_squad.ui.dialog.PermissionRationaleDialog;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.AddCommentView;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.CommentsContainer;
import com.babushka.slav_squad.ui.screens.image_preview.ImagePreviewActivity;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;
import com.babushka.slav_squad.ui.screens.likes.model.LikesModel;
import com.babushka.slav_squad.ui.screens.likes.view.LikesActivity;
import com.babushka.slav_squad.ui.screens.main.model.MainModel;
import com.babushka.slav_squad.ui.screens.post_preview.PostPreviewContract;
import com.babushka.slav_squad.ui.screens.post_preview.model.PostPreviewModel;
import com.babushka.slav_squad.ui.screens.post_preview.presenter.PostPreviewPresenter;
import com.babushka.slav_squad.ui.screens.post_preview.view.custom_view.LikesCirclesContainer;
import com.babushka.slav_squad.util.IntentBuilder;
import com.google.gson.Gson;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by iliyan on 26.09.17.
 */
@RuntimePermissions
public class PostPreviewActivity extends BaseActionBarActivity<PostPreviewContract.Presenter>
        implements PostPreviewContract.View, AddCommentView.Listener, LikesContract.View {
    //TODO: Refactor: remove redundancy with MainActivity and preserve MVP pattern
    private static final String EXTRA_POST = "post_extra";

    @BindView(R.id.activity_post_preview)
    LockableNestedScrollView vRootScroll;
    @BindView(R.id.post_preview_description_label_text_view)
    TextView vDescLabel;
    @BindView(R.id.post_preview_aspect_ratio_photo_view)
    AspectRatioPhotoView vPostImage;
    @BindView(R.id.post_preview_likes_count_text_view)
    TextView vLikesCountText;
    @BindView(R.id.post_preview_description_text_view)
    TextView vDescriptionText;
    @BindView(R.id.post_preview_likes_container)
    LikesCirclesContainer vLikesContainer;
    @BindView(R.id.post_preview_comments_container)
    CommentsContainer vCommentsContainer;
    @BindView(R.id.post_preview_add_comment_view)
    AddCommentView vAddComment;
    @BindView(R.id.post_likes_count_text_view)
    TextView vUpperLikesCountText;
    @BindView(R.id.post_like_image_button)
    ImageButton vLikeButton;
    private Post mPost;
    @Nullable
    private MaterialDialog mProgressDialog;

    public static void startScreen(@NonNull Context context, @NonNull Post post) {
        Intent intent = new Intent(context, PostPreviewActivity.class);
        intent.putExtra(EXTRA_POST, new Gson().toJson(post));
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_post_preview;
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mPost = new Gson().fromJson(intent.getStringExtra(EXTRA_POST), Post.class);
        if (mPost == null) {
            throw new IllegalArgumentException("PostPreviewActivity requires '" + EXTRA_POST + "' to start!");
        }
    }

    @NonNull
    @Override
    protected PostPreviewContract.Presenter initializePresenter() {
        return new PostPreviewPresenter(this, new PostPreviewModel(mPost),
                new LikesModel(), this, mPost, getSimpleAnalytics(),
                new MainModel(this, SessionManager.getInstance().getCurrentFirebaseUser()));
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vCommentsContainer.setup(this, mPost);
        vAddComment.setListener(this);
        vLikesContainer.setup(this);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupUI();
        mPresenter.displayCommentsInRealTime();
        vPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.startScreen(PostPreviewActivity.this, mPost.getImage().getImageUrl());
            }
        });
    }

    @Override
    public void addComment(@NonNull Comment comment) {
        vCommentsContainer.add(comment);
    }

    @Override
    public void updateComment(@NonNull Comment comment) {
        vCommentsContainer.update(comment);
    }

    @Override
    public void removeComment(@NonNull Comment comment) {
        vCommentsContainer.remove(comment);
    }

    @Override
    public void setupAddCommentUI(@NonNull User currentUser) {
        vAddComment.setup(currentUser);
    }

    @Override
    public void setPostLiked(boolean liked) {
        vLikeButton.setImageResource(liked ? R.drawable.ic_like_filled
                : R.drawable.ic_like);
    }

    @Override
    public void displayPostImage(@NonNull Post.Image image) {
        vPostImage.display(image, GlideApp.with(this));
    }

    @Override
    public void displayDescription(@NonNull String description) {
        if (TextUtils.isEmpty(description)) {
            vDescriptionText.setVisibility(View.GONE);
            vDescLabel.setVisibility(View.GONE);
        } else {
            vDescriptionText.setText(description);
        }
    }

    @Override
    public void displayLikesCount(int likesCount) {
        vLikesCountText.setText(getResources().getQuantityString(R.plurals.likes_count, likesCount, likesCount));
        vUpperLikesCountText.setText(String.valueOf(likesCount));
    }

    @Override
    public void showToast(@NonNull String message) {
        super.showToast(message);
    }

    @Override
    public void openLikesScreen(@NonNull Post post) {
        LikesActivity.startScreen(this, post);
    }

    @Override
    public void onLikeAdded(@NonNull UserBase user) {
        vLikesContainer.add(user);
    }

    @Override
    public void onLikeChanged(@NonNull UserBase user) {
        vLikesContainer.update(user);
    }

    @Override
    public void onLikeRemoved(@NonNull UserBase user) {
        vLikesContainer.remove(user);
    }

    @Override
    public void onSendComment(@NonNull Comment comment) {
        mPresenter.addComment(comment);
        vRootScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @OnClick({R.id.post_preview_likes_container, R.id.post_preview_likes_count_text_view,
            R.id.post_preview_liked_by_label_text_view})
    public void onShowLikesScreen() {
        LikesActivity.startScreen(this, mPost);
    }

    @OnClick({R.id.post_like_image_button, R.id.post_like_click_area})
    public void onLikeClick() {
        mPresenter.toggleLike();
    }


    @Nullable
    @Override
    protected String getScreenName() {
        return EventValues.Screen.POST_PREVIEW;
    }

    @Override
    public void downloadPostWithPermissionCheck(@NonNull String imageUrl) {
        PostPreviewActivityPermissionsDispatcher.downloadPostWithCheck(this);
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
    public void downloadPost() {
        logFromScreenEvent(Events.Permission.WRITE_STORAGE_GRANTED);
        mPresenter.downloadPost();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForWriteStorage(@NonNull PermissionRequest request) {
        logFromScreenEvent(Events.Permission.WRITE_STORAGE_RATIONALE);
        new PermissionRationaleDialog(getString(R.string.permission_write_storage_rationale_title),
                getString(R.string.permission_write_storage_rationale_content), request).show(this);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showDeniedForWriteStorage() {
        logFromScreenEvent(Events.Permission.WRITE_STORAGE_DENY);
        new PermissionDenyDialog(getString(R.string.permission_write_storage_deny_title),
                getString(R.string.permission_write_storage_deny_content)).show(this);

    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showNeverAskForWriteStorage() {
        logFromScreenEvent(Events.Permission.WRITE_STORAGE_NEVER_ASK);
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

    @OnClick({R.id.post_download_image_button, R.id.post_download_click_area})
    public void onDownloadClick() {
        logFromScreenEvent(Events.Main.POST_DOWNLOAD);
        downloadPostWithPermissionCheck(mPost.getImage().getImageUrl());
    }

    @OnClick({R.id.post_share_image_button, R.id.post_share_click_area})
    public void onShareClick() {
        //TODO: Move business logic to presenter
        logFromScreenEvent(Events.Main.POST_SHARE_CLICK);
        if (mPost != null) {
            String imageUrl = mPost.getImage().getImageUrl();
            Intent shareIntent = IntentBuilder.buildShareIntent(imageUrl);
            this.startActivity(shareIntent);
        }
    }

    @OnClick({R.id.post_likes_count_text_view, R.id.post_likes_count_click_area})
    public void onLikesCountClick() {
        //TODO: Move business logic to presenter
        logFromScreenEvent(Events.Main.POST_LIKES_COUNT_CLICK);
        mPresenter.handlePostLikesCountClick();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PostPreviewActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
