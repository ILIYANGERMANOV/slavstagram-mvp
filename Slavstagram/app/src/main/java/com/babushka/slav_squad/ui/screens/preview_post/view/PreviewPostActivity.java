package com.babushka.slav_squad.ui.screens.preview_post.view;

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
import com.babushka.slav_squad.ui.screens.preview_post.PreviewPostContract;
import com.babushka.slav_squad.ui.screens.preview_post.model.PreviewPostModel;
import com.babushka.slav_squad.ui.screens.preview_post.presenter.PreviewPostPresenter;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by iliyan on 26.09.17.
 */

public class PreviewPostActivity extends BaseActionBarActivity<PreviewPostContract.Presenter>
        implements PreviewPostContract.View, AddCommentView.Listener {
    private static final String EXTRA_POST = "post_extra";

    @BindView(R.id.activity_preview_post)
    LockableNestedScrollView vRootScroll;
    @BindView(R.id.preview_post_aspect_ratio_photo_view)
    AspectRatioPhotoView vPostImage;
    @BindView(R.id.preview_post_likes_count_text_view)
    TextView vLikesCountText;
    @BindView(R.id.preview_post_description_text_view)
    TextView vDescriptionText;
    @BindView(R.id.preview_post_comments_container)
    CommentsContainer vCommentsContainer;
    @BindView(R.id.preview_post_add_comment_view)
    AddCommentView vAddComment;
    private Post mPost;

    public static void startScreen(@NonNull Context context, @NonNull Post post) {
        Intent intent = new Intent(context, PreviewPostActivity.class);
        intent.putExtra(EXTRA_POST, new Gson().toJson(post));
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_preview_post;
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mPost = new Gson().fromJson(intent.getStringExtra(EXTRA_POST), Post.class);
        if (mPost == null) {
            throw new IllegalArgumentException("PreviewPostActivity requires '" + EXTRA_POST + "' to start!");
        }
    }

    @NonNull
    @Override
    protected PreviewPostContract.Presenter initializePresenter() {
        return new PreviewPostPresenter(this,
                new PreviewPostModel(mPost), mPost);
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vCommentsContainer.setup(this);
        vAddComment.setListener(this);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupUI();
        mPresenter.displayCommentsInRealTime();
//        vPostImage.setOnTouchListener(new View.OnTouchListener() {
//            private ScaleGestureDetector mDetector = new ScaleGestureDetector(PreviewPostActivity.this, new ScaleGestureDetector.OnScaleGestureListener() {
//                @Override
//                public boolean onScale(ScaleGestureDetector detector) {
//                    vRootScroll.smoothScrollTo(0, 0);
//                    vRootScroll.setScrollingEnabled(false);
//                    return false;
//                }
//
//                @Override
//                public boolean onScaleBegin(ScaleGestureDetector detector) {
//                    vRootScroll.smoothScrollTo(0, 0);
//                    vRootScroll.setScrollingEnabled(false);
//                    return false;
//                }
//
//                @Override
//                public void onScaleEnd(ScaleGestureDetector detector) {
//                    vRootScroll.setScrollingEnabled(true);
//                }
//            });
//
//            private GestureDetector mGestureDetector = new GestureDetector(PreviewPostActivity.this, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//                    return super.onFling(e1, e2, velocityX, velocityY);
//                }
//            });
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return mDetector.onTouchEvent(event);
//            }
//        });
        vPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewActivity.startScreen(PreviewPostActivity.this, mPost.getImage().getImageUrl());
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
    public void displayPostLikes(int likesCount) {
        vLikesCountText.setText("Likes: " + likesCount);
    }

    @Override
    public void displayDescription(@NonNull String description) {
        if (TextUtils.isEmpty(description)) {
            vDescriptionText.setVisibility(View.GONE);
        } else {
            vDescriptionText.setText(description);
        }
    }

    @Override
    public void onSendComment(@NonNull Comment comment) {
        mPresenter.addComment(comment);
        vRootScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
