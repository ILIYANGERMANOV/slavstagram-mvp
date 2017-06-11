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
import com.babushka.slav_squad.util.datetime.DateType;
import com.babushka.slav_squad.util.datetime.periods.PastTimePeriod;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        ButterKnife.bind(this, itemView);
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
        PastTimePeriod timePeriod = new PastTimePeriod(new Date(timestamp));
        DateType dateType = timePeriod.getDateType();
        if (dateType != null) {
            int stringResId;
            switch (dateType) {
                case SECONDS:
                    vDateText.setText(R.string.just_now);
                    return;
                case MINUTES:
                    stringResId = R.string.minutes_ago;
                    break;
                case HOURS:
                    stringResId = R.string.hours_ago;
                    break;
                case DAYS:
                    stringResId = R.string.days_ago;
                    break;
                case WEEKS:
                    stringResId = R.string.weeks_ago;
                    break;
                case MONTHS:
                    stringResId = R.string.months_ago;
                    break;
                default:
                    //UNKNOWN, do nothing
                    return;
            }
            int quantity = timePeriod.getQuantity();
            String formattedTime = mContext.getString(stringResId, quantity);
            vDateText.setText(formattedTime);
        }
    }

    @Override
    public void resetState() {
        vAuthorImage.setImageDrawable(null);
        vDateText.setText("");
    }
}
