package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 30.05.17.
 */

public class PostView extends LinearLayout {
    private static int sPostViewWidth = 0;

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

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupRootView();
        inflate(context, R.layout.post_view_layout, this);
        ButterKnife.bind(this);
    }

    private void setupRootView() {
        setOrientation(VERTICAL);
    }

    public void display(@NonNull Post post, GlideRequests imageLoader) {
        //TODO: Consider adding defense against missing fields
        displayAuthor(post.getAuthor(), imageLoader);
        displayPostImage(post.getImage(), imageLoader);
        vDescriptionText.setText(post.getDescription());
        vLikesCountText.setText(String.valueOf(post.getLikesCount()));
        displayComments(post.getComments());
    }

    private void displayAuthor(@NonNull User author, GlideRequests imageLoader) {
        imageLoader.load(author.getPhotoUrl())
                .dontAnimate()
                //TODO: add placeholder and error drawable
                .into(vAuthorCircleImage);
        vAuthorNameText.setText(author.getDisplayName());
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

    private void displayComments(@Nullable Map<String, Boolean> comments) {
        if (comments != null) {
            vCommentsCountText.setText(String.valueOf(comments.size()));
        }
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
        vAuthorCircleImage.setImageDrawable(null);
        vAuthorNameText.setText("");
        vMainImage.setImageDrawable(null);
        vDescriptionText.setText("");
        vCommentsCountText.setText("0");
        vLikesCountText.setText("0");
    }


    @OnClick(R.id.post_like_image_button)
    public void onLikeClicked() {
        //TODO: Implement method
    }

    @OnClick(R.id.post_comments_image_button)
    public void onCommentsClicked() {
        //TODO: Implement method
    }

    @OnClick(R.id.post_share_image_button)
    public void onShareClicked() {
        //TODO: Implement method
    }

    @OnClick(R.id.post_download_image_button)
    public void onDownloadClicked() {
        //TODO: Implement method
    }
}
