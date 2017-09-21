package com.babushka.slav_squad.ui.screens.profile.view.custom_view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.BuildConfig;
import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.BaseAdapter;

import butterknife.BindView;

/**
 * Created by iliyan on 08.06.17.
 */

public class ProfilePostViewHolder extends BaseAdapter.BaseViewHolder<Post> {
    @NonNull
    private final GlideRequests mImageLoader;
    @BindView(R.id.adapter_profile_post_image_view)
    ImageView vPostImage;
    @Nullable
    private Post mPost;

    ProfilePostViewHolder(View itemView, @NonNull GlideRequests imageLoader,
                          boolean isMyProfile) {
        super(itemView);
        mImageLoader = imageLoader;

        if (isMyProfile || BuildConfig.DEBUG) {
            addPostDeleteOption(itemView);
        }
    }

    private void addPostDeleteOption(final View itemView) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(itemView.getContext())
                        .title("Delete post?")
                        .positiveText("DELETE")
                        .negativeText("CANCEL")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (mPost != null) {
                                    Database.getInstance().deletePost(mPost);
                                }
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public void display(@NonNull Post post) {
        mPost = post;
        mImageLoader.load(mPost.getImage().getImageUrl())
                .into(vPostImage);
    }

    @Override
    public void resetState() {
        vPostImage.setImageDrawable(null);
    }
}
