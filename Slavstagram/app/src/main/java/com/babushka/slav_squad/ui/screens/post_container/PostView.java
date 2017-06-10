package com.babushka.slav_squad.ui.screens.post_container;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.comments.view.CommentsActivity;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 30.05.17.
 */

public class PostView extends LinearLayout {
    private static int sPostViewWidth = 0;
    @NonNull
    private final GestureDetector mGestureDetector;

    @BindView(R.id.post_author_layout)
    LinearLayout vAuthorLayout;
    @BindView(R.id.post_author_circle_image_view)
    CircleImageView vAuthorCircleImage;
    @BindView(R.id.post_author_name_text_view)
    TextView vAuthorNameText;
    @BindView(R.id.post_main_image_view)
    ImageView vMainImage;
    @BindView(R.id.post_likes_count_text_view)
    TextView vLikesCountText;
    @BindView(R.id.post_comments_count_text_view)
    TextView vCommentsCountText;
    @BindView(R.id.post_description_text_view)
    TextView vDescriptionText;
    @BindView(R.id.post_like_image_button)
    ImageButton vLikeButton;

    @Nullable
    private Post mPost;
    private boolean mHideAuthor;
    @Nullable
    private OnPostLongClickListener mOnPostLongClickListener;

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupUI(context);
        setupFromAttributes(context, attrs);
        mGestureDetector = new GestureDetector(context, new PostGestureListener());
        setup();
    }

    public void setOnPostLongClickListener(@Nullable OnPostLongClickListener onPostLongClickListener) {
        mOnPostLongClickListener = onPostLongClickListener;
    }

    private void setupUI(Context context) {
        setupRootView();
        inflate(context, R.layout.post_view_layout, this);
        ButterKnife.bind(this);
    }

    private void setupFromAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PostView);
        try {
            mHideAuthor = ta.getBoolean(R.styleable.PostView_hideAuthor, false);
            vAuthorLayout.setVisibility(mHideAuthor ? GONE : VISIBLE);
        } finally {
            ta.recycle();
        }
    }

    private void setupRootView() {
        setOrientation(VERTICAL);
    }

    private void setup() {
        vMainImage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    public void display(@NonNull Post post, GlideRequests imageLoader) {
        mPost = post;
        //TODO: Consider adding defense against missing fields
        if (!mHideAuthor) {
            displayAuthor(post.getAuthor(), imageLoader);
        }
        displayPostImage(post.getImage(), imageLoader);
        displayDescription(post.getDescription());
        displayLikes(post);
        displayComments(post.getCommentsCount());
    }

    private void displayPostImage(@NonNull final Post.Image image, @NonNull final GlideRequests imageLoader) {
        if (sPostViewWidth == 0) {
            vMainImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    sPostViewWidth = getMeasuredWidth();
                    vMainImage.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                    resizeImageViewAndLoadImage(image, imageLoader);
                }
            });
        } else {
            resizeImageViewAndLoadImage(image, imageLoader);
        }
    }

    private void displayDescription(@Nullable String description) {
        if (description != null) {
            vDescriptionText.setText(description);
        } else {
            vDescriptionText.setVisibility(GONE);
        }
    }

    private void displayLikes(@NonNull Post post) {
        vLikesCountText.setText(String.valueOf(post.getLikesCount()));
        int likeImageRID = post.isLiked() ? R.drawable.ic_favorite_black_36dp
                : R.drawable.ic_favorite_border_black_36dp;
        vLikeButton.setImageResource(likeImageRID);
    }

    private void displayAuthor(@NonNull User author, GlideRequests imageLoader) {
        imageLoader.load(author.getPhotoUrl())
                .dontAnimate()
                //TODO: add placeholder and error drawable
                .into(vAuthorCircleImage);
        vAuthorNameText.setText(author.getDisplayName());
    }

    private void displayComments(int commentsCount) {
        vCommentsCountText.setText(String.valueOf(commentsCount));
    }

    private void resizeImageViewAndLoadImage(@NonNull Post.Image postImage, @NonNull GlideRequests imageLoader) {
        int targetImageHeight = (int) (sPostViewWidth / postImage.getWidthHeightRatio());
        setImageViewHeight(vMainImage, targetImageHeight);
        imageLoader.load(postImage.getImageUrl())
                .override(sPostViewWidth, targetImageHeight)
                //TODO: add placeholder and error drawable
                .into(vMainImage);
    }

    private void setImageViewHeight(@NonNull ImageView targetImageView, int height) {
        targetImageView.getLayoutParams().height = height;
        targetImageView.requestLayout();
    }

    public void reset() {
        mPost = null;
        vAuthorCircleImage.setImageDrawable(null);
        vAuthorNameText.setText("");
        vMainImage.setImageDrawable(null);
        vDescriptionText.setText("");
        vDescriptionText.setVisibility(VISIBLE);
        vCommentsCountText.setText("0");
        vLikesCountText.setText("0");
    }


    @OnClick(R.id.post_like_image_button)
    public void onLikeClicked() {
        if (mPost != null) {
            if (mPost.isLiked()) {
                //post is liked, unlike it
                unlikePost(mPost);
            } else {
                //post is NOT liked, like it
                likePost(mPost);
            }
        }
    }

    private void likePost(@NonNull Post post) {
        if (!post.isLiked()) {
            vLikeButton.setImageResource(R.drawable.ic_favorite_black_36dp);
            setPostLikedState(post, true);
        }
    }

    private void unlikePost(@NonNull Post post) {
        if (post.isLiked()) {
            vLikeButton.setImageResource(R.drawable.ic_favorite_border_black_36dp);
            setPostLikedState(post, false);
        }
    }

    private void setPostLikedState(@NonNull Post post, boolean liked) {
        post.setLiked(liked);
        FirebaseUser user = SessionManager.getInstance().getCurrentUser();
        Database.getInstance().toggleLike(post, user.getUid());
    }

    @OnClick(R.id.post_comments_image_button)
    public void onCommentsClicked() {
        if (mPost != null) {
            String text = "Cyka blyat!";
            switch (new Random().nextInt(3)) {
                case 0:
                    text = "Idi nahui!";
                    break;
                case 1:
                    text = "Approves";
                    break;
                case 2:
                    text = "Kurwaa";
                    break;
            }
            FirebaseUser firebaseUser = SessionManager.getInstance().getCurrentUser();
            Comment comment = new Comment(new User(firebaseUser), text);
            Database.getInstance().addComment(mPost, comment);
            //TODO: Implement method

            CommentsActivity.startScreen(getContext(), mPost);
        }
    }

    @OnClick(R.id.post_share_image_button)
    public void onShareClicked() {
        //TODO: Implement method
    }

    @OnClick(R.id.post_download_image_button)
    public void onDownloadClicked() {
        if (mPost != null) {
            //TODO: Implement method
        }
    }

    @OnClick(R.id.post_author_layout)
    public void onAuthorLayoutClicked() {
        if (mPost != null) {
            User author = mPost.getAuthor();
            ProfileActivity.startScreen(getContext(), author);
        }
    }

    public interface OnPostLongClickListener {
        void onPostLongClick(@NonNull Post post);
    }

    private class PostGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mPost != null) {
                likePost(mPost);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mOnPostLongClickListener != null && mPost != null) {
                mOnPostLongClickListener.onPostLongClick(mPost);
            }
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //TODO: Implement method
            return true;
        }
    }
}