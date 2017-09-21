package com.babushka.slav_squad.ui.screens.comments.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.container.BaseAdapter;
import com.babushka.slav_squad.ui.screens.util.TimeAgo;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 11.06.17.
 */

public class CommentViewHolder extends BaseAdapter.BaseViewHolder<Comment> {
    @NonNull
    private final GlideRequests mImageLoader;
    @NonNull
    private final Context mContext;
    @BindView(R.id.comment_author_circle_image_view)
    CircleImageView vAuthorImage;
    @BindView(R.id.comment_author_name_text_view)
    TextView vAuthorNameText;
    @BindView(R.id.comment_content_text_view)
    TextView vCommentText;
    @BindView(R.id.comment_date_text_view)
    TextView vDateText;

    public CommentViewHolder(View itemView, @NonNull GlideRequests imageLoader) {
        super(itemView);
        mImageLoader = imageLoader;
        mContext = itemView.getContext();
    }

    @Override
    public void display(@NonNull Comment comment) {
        displayAuthor(comment.getAuthor());
        vCommentText.setText(comment.getText());
        displayCreationDate(comment.getTimestamp());
    }

    private void displayAuthor(@NonNull User author) {
        mImageLoader.load(author.getPhotoUrl())
                .dontAnimate()
                //TODO: add placeholder and error drawable
                .into(vAuthorImage);
        vAuthorNameText.setText(author.getDisplayName());
    }

    private void displayCreationDate(long timestamp) {
        TimeAgo timeAgo = new TimeAgo(mContext, timestamp);
        timeAgo.display(vDateText);
    }

    @Override
    public void resetState() {
        vAuthorImage.setImageDrawable(null);
        vDateText.setText("");
    }
}
