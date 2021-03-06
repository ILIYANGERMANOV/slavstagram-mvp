package com.babushka.slav_squad.ui.screens.post_container;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
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
import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventBuilder;
import com.babushka.slav_squad.analytics.event.EventParamKeys;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.event.DownloadPostEvent;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.comments.view.CommentsActivity;
import com.babushka.slav_squad.ui.screens.likes.view.LikesActivity;
import com.babushka.slav_squad.ui.screens.post_preview.view.PostPreviewActivity;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;
import com.babushka.slav_squad.ui.screens.util.TimeAgo;
import com.babushka.slav_squad.util.AppUtil;
import com.babushka.slav_squad.util.IntentBuilder;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 30.05.17.
 */

public class PostView extends CardView {
    private static final int MILLION = 1000000;
    private static final int THOUSAND = 1000;
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
    @BindView(R.id.post_comment_count_text_view)
    TextView vCommentsCountText;
    @BindView(R.id.post_description_text_view)
    TextView vDescriptionText;
    @BindView(R.id.post_like_image_button)
    ImageButton vLikeButton;
    @BindView(R.id.post_comment_image_button)
    ImageButton vCommentButton;
    @BindView(R.id.post_date_text_view)
    TextView vDateText;

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
        inflate(context, R.layout.post_view, this);
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
        setRadius(AppUtil.dpToPx(getContext(), 6));
        setCardElevation(AppUtil.dpToPx(getContext(), 4));
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
        displayCreationDate(post.getTimestamp());
    }

    private void displayCreationDate(long timestamp) {
        TimeAgo timeAgo = new TimeAgo(getContext(), timestamp);
        timeAgo.display(vDateText);
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
        if (TextUtils.isEmpty(description)) {
            vDescriptionText.setVisibility(GONE);
        } else {
            vDescriptionText.setText(description);
        }
    }

    private void displayLikes(@NonNull Post post) {
        int likesCount = post.getLikesCount();
        String likesLabel;
        if (likesCount >= MILLION) {
            likesLabel = String.valueOf(likesCount / MILLION) + "m";
        } else if (likesCount > THOUSAND) {
            likesLabel = String.valueOf(likesCount / THOUSAND) + "k";
        } else {
            likesLabel = String.valueOf(likesCount);
        }
        vLikesCountText.setText(likesLabel);
        int likeImageRID = post.isLiked() ? R.drawable.ic_like_filled
                : R.drawable.ic_like;
        vLikeButton.setImageResource(likeImageRID);
    }

    private void displayAuthor(@NonNull UserBase author, GlideRequests imageLoader) {
        imageLoader.load(author.getPhotoUrl())
                .dontAnimate()
                //TODO: add placeholder and error drawable
                .into(vAuthorCircleImage);
        vAuthorNameText.setText(author.getDisplayName());
    }

    private void displayComments(int commentsCount) {
        vCommentsCountText.setText(String.valueOf(commentsCount));
        int commentIconRID = commentsCount > 0 ? R.drawable.ic_comment_filled
                : R.drawable.ic_comment;
        vCommentButton.setImageResource(commentIconRID);
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


    @OnClick({R.id.post_like_image_button, R.id.post_like_click_area})
    public void onLikeClick() {
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
            logPostEvent(Events.Main.POST_LIKE);
            vLikeButton.setImageResource(R.drawable.ic_like_filled);
            setPostLikedState(post, true);
        }
    }

    private void unlikePost(@NonNull Post post) {
        if (post.isLiked()) {
            logPostEvent(Events.Main.POST_UNLIKE);
            vLikeButton.setImageResource(R.drawable.ic_like);
            setPostLikedState(post, false);
        }
    }

    private void setPostLikedState(@NonNull Post post, boolean liked) {
        post.setLiked(liked);
        User user = SessionManager.getInstance().getCurrentUser();
        Database.getInstance().toggleLike(post, user);
    }

    @OnClick({R.id.post_likes_count_text_view, R.id.post_likes_count_click_area})
    public void onLikesCountClick() {
        logPostEvent(Events.Main.POST_LIKES_COUNT_CLICK);
        if (mPost != null) {
            LikesActivity.startScreen(getContext(), mPost);
        }
    }

    @OnClick({R.id.post_comment_image_button, R.id.post_comment_click_area})
    public void onCommentsClicked() {
        logPostEvent(Events.Main.POST_COMMENT_CLICK);
        if (mPost != null) {
            CommentsActivity.startScreen(getContext(), mPost);
        }
    }

    @OnClick({R.id.post_share_image_button, R.id.post_share_click_area})
    public void onShareClick() {
        logPostEvent(Events.Main.POST_SHARE_CLICK);
        if (mPost != null) {
            Context context = getContext();
            String imageUrl = mPost.getImage().getImageUrl();
            Intent shareIntent = IntentBuilder.buildShareIntent(imageUrl);
            context.startActivity(shareIntent);
        }
    }

    @OnClick({R.id.post_download_image_button, R.id.post_download_click_area})
    public void onDownloadClick() {
        logPostEvent(Events.Main.POST_DOWNLOAD);
        if (mPost != null) {
            EventBus.getDefault().post(new DownloadPostEvent(mPost.getImage().getImageUrl()));
        }
    }

    @OnClick(R.id.post_author_layout)
    public void onAuthorLayoutClick() {
        if (mPost != null) {
            MyApp.logEvent(new EventBuilder()
                    .setEventName(Events.Main.POST_OPEN_PROFILE)
                    .addParam(EventParamKeys.POST_ID, mPost.getId())
                    .addParam(EventParamKeys.USER_ID, mPost.getAuthor().getId())
                    .build());
            UserBase author = mPost.getAuthor();
            ProfileActivity.startScreen(getContext(), author);
        }
    }

    private void logPostEvent(@NonNull String eventName) {
        EventBuilder builder = new EventBuilder()
                .setEventName(eventName);
        if (mPost != null) {
            builder.addParam(EventParamKeys.POST_ID, mPost.getId());
        }
        MyApp.logEvent(builder.build());
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
            logPostEvent(Events.Main.POST_LIKE_DOUBLE_TAP);
            if (mPost != null) {
                likePost(mPost);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            logPostEvent(Events.Main.POST_IMAGE_LONG_CLICK);
            if (mOnPostLongClickListener != null && mPost != null) {
                mOnPostLongClickListener.onPostLongClick(mPost);
            }
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            logPostEvent(Events.Main.POST_PREVIEW);
            if (mPost != null) {
                PostPreviewActivity.startScreen(getContext(), mPost);
            }
            return true;
        }
    }
}
