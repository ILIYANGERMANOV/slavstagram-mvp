package com.babushka.slav_squad.ui.screens.likes.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.container.BaseAdapter;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 09.11.17.
 */

public class LikeViewHolder extends BaseAdapter.BaseViewHolder<UserBase> implements View.OnClickListener {
    @NonNull
    private final GlideRequests mImageLoader;
    @NonNull
    private final Context mContext;
    @BindView(R.id.like_user_circle_image_view)
    CircleImageView vUserImage;
    @BindView(R.id.like_display_name_text_view)
    TextView vDisplayNameText;
    private UserBase mUser;

    public LikeViewHolder(View itemView, @NonNull GlideRequests imageLoader) {
        super(itemView);
        mImageLoader = imageLoader;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void display(@NonNull UserBase user) {
        mUser = user;
        mImageLoader.load(user.getPhotoUrl())
                .dontAnimate()
                .into(vUserImage);
        vDisplayNameText.setText(user.getDisplayName());
    }

    @Override
    public void resetState() {
        vUserImage.setImageDrawable(null);
        vDisplayNameText.setText("");
    }

    @Override
    public void onClick(View v) {
        if (mUser != null) {
            ProfileActivity.startScreen(mContext, mUser);
        }
    }
}
