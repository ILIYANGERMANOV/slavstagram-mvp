package com.babushka.slav_squad.ui.screens.post_preview.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.custom_view.AspectRatioPhotoView;
import com.babushka.slav_squad.ui.custom_view.LockableNestedScrollView;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.AddCommentView;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.CommentsContainer;
import com.babushka.slav_squad.ui.screens.image_preview.ImagePreviewActivity;
import com.babushka.slav_squad.ui.screens.likes.view.LikesActivity;
import com.babushka.slav_squad.ui.screens.post_preview.PostPreviewContract;
import com.babushka.slav_squad.ui.screens.post_preview.model.PostPreviewModel;
import com.babushka.slav_squad.ui.screens.post_preview.presenter.PostPreviewPresenter;
import com.babushka.slav_squad.ui.screens.post_preview.view.custom_view.LikesCirclesContainer;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by iliyan on 26.09.17.
 */

public class PostPreviewActivity extends BaseActionBarActivity<PostPreviewContract.Presenter>
        implements PostPreviewContract.View, AddCommentView.Listener {
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
    private Post mPost;

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
        return new PostPreviewPresenter(this,
                new PostPreviewModel(mPost), mPost);
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
    public void displayPostImage(@NonNull Post.Image image) {
        vPostImage.display(image, GlideApp.with(this));
    }

    @Override
    public void displayPostLikesCount(int likesCount) {
        vLikesCountText.setText(getString(R.string.post_preview_squats_count, likesCount));
    }

    @Override
    public void displayPostLikes(@NonNull List<User> likes) {
        vLikesContainer.display(likes);
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
    public void onSendComment(@NonNull Comment comment) {
        mPresenter.addComment(comment);
        vRootScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @OnClick({R.id.post_preview_likes_container, R.id.post_preview_likes_count_text_view,
            R.id.post_preview_liked_by_label_text_view})
    public void onShowLikesScreen() {
        LikesActivity.startScreen(this, mPost);
    }
}
