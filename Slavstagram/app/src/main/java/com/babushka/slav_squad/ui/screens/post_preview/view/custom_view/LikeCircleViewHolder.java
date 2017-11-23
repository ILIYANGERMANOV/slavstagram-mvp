package com.babushka.slav_squad.ui.screens.post_preview.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.container.BaseAdapter;
import com.babushka.slav_squad.ui.screens.profile.view.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 22.11.17.
 */

public class LikeCircleViewHolder extends BaseAdapter.BaseViewHolder<User> implements View.OnClickListener {
    @NonNull
    private final GlideRequests mImageLoader;
    @NonNull
    private final Context mContext;
    @BindView(R.id.like_circle_circle_image_view)
    CircleImageView vUserImage;
    private User mUser;

    LikeCircleViewHolder(View itemView, @NonNull GlideRequests imageLoader) {
        super(itemView);
        mImageLoader = imageLoader;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void display(@NonNull User user) {
        mUser = user;
        mImageLoader.load(user.getPhotoUrl())
                .dontAnimate()
                .into(vUserImage);
    }

    @Override
    public void resetState() {
        vUserImage.setImageDrawable(null);
    }

    @Override
    public void onClick(View v) {
        if (mUser != null) {
            ProfileActivity.startScreen(mContext, mUser);
        }
    }
}
