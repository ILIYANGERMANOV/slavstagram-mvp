package com.babushka.slav_squad.ui.screens.comments.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.container.BaseAdapter;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;
import com.babushka.slav_squad.ui.screens.util.TimeAgo;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 11.06.17.
 */

public class CommentViewHolder extends BaseAdapter.BaseViewHolder<Comment> implements View.OnLongClickListener {
    @NonNull
    private final GlideRequests mImageLoader;
    @NonNull
    private final Context mContext;
    @NonNull
    private final Post mPost;
    @BindView(R.id.comment_author_circle_image_view)
    CircleImageView vAuthorImage;
    @BindView(R.id.comment_author_name_text_view)
    TextView vAuthorNameText;
    @BindView(R.id.comment_content_text_view)
    TextView vCommentText;
    @BindView(R.id.comment_date_text_view)
    TextView vDateText;

    @Nullable
    private Comment mComment;

    public CommentViewHolder(View itemView, @NonNull GlideRequests imageLoader,
                             @NonNull Post post) {
        super(itemView);
        mImageLoader = imageLoader;
        mContext = itemView.getContext();
        mPost = post;
        itemView.setOnLongClickListener(this);
    }

    @OnClick(R.id.comment_author_circle_image_view)
    public void onProfilePicClick() {
        if (mComment != null) {
            ProfileActivity.startScreen(mContext, mComment.getAuthor());
        }
    }

    @Override
    public void display(@NonNull Comment comment) {
        mComment = comment;
        displayAuthor(comment.getAuthor());
        vCommentText.setText(comment.getText());
        displayCreationDate(comment.getTimestamp());
    }

    private void displayAuthor(@NonNull UserBase author) {
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

    @Override
    public boolean onLongClick(View v) {
        if (mComment != null) {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser.equals(mComment.getAuthor())) {
                new MaterialDialog.Builder(mContext)
                        .title("Delete comment?")
                        .positiveText("DELETE")
                        .positiveColorRes(R.color.red)
                        .neutralText("CANCEL")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Database.getInstance().deleteComment(mPost, mComment);
                            }
                        })
                        .show();
                return true;
            }
        }
        return false;
    }
}
