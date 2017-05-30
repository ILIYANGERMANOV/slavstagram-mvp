package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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

public class PostView extends ConstraintLayout {
    //TODO: Refactor
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
        inflate(context, R.layout.post_view_layout, this);
        ButterKnife.bind(this);
    }

    public void display(@NonNull Post post, GlideRequests imageLoader) {
        //TODO: Consider adding defense agains missing fields
        displayAuthor(post.getAuthor(), imageLoader);
        displayPostImage(post.getImage(), imageLoader);
        vDescriptionText.setText(post.getDescription());
        vLikesCountText.setText(String.valueOf(post.getLikesCount()));
        Map<String, Boolean> comments = post.getComments();
        if (comments != null) {
            vCommentsCountText.setText(String.valueOf(comments.size()));
        }
    }

    private void displayAuthor(@NonNull User author, GlideRequests imageLoader) {
        imageLoader.load(author.getPhotoUrl()).into(vAuthorCircleImage);
        vAuthorNameText.setText(author.getDisplayName());
    }

    private void displayPostImage(@NonNull final Post.Image image, @NonNull final GlideRequests imageLoader) {
        //TODO: Refactor
        int measuredWidth = vMainImage.getMeasuredWidth();
        if (measuredWidth > 0) {
            resizeImageViewAndLoadImage(image, imageLoader);
        } else {
            vMainImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    vMainImage.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                    resizeImageViewAndLoadImage(image, imageLoader);
                }
            });
        }
    }

    private void resizeImageViewAndLoadImage(@NonNull Post.Image postImage, @NonNull GlideRequests imageLoader) {
        int imageWidth = vMainImage.getMeasuredWidth();
        int targetImageHeight = (int) (imageWidth / postImage.getWidthHeightRatio());
        setImageViewHeight(vMainImage, targetImageHeight);
        imageLoader.load(postImage.getImageUrl())
                .override(imageWidth, targetImageHeight)
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
