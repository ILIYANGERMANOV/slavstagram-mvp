package com.babushka.slav_squad.ui.screens.profile.view.custom_view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.RemoteConfig;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.container.BaseAdapter;
import com.babushka.slav_squad.ui.custom_view.AspectRatioImageView;
import com.babushka.slav_squad.ui.screens.post_preview.view.PostPreviewActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by iliyan on 08.06.17.
 */

public class ProfilePostViewHolder extends BaseAdapter.BaseViewHolder<Post> implements View.OnClickListener {
    @NonNull
    private final GlideRequests mImageLoader;
    @BindView(R.id.adapter_profile_post_image_view)
    AspectRatioImageView vPostImage;
    @Nullable
    private Post mPost;

    ProfilePostViewHolder(View itemView, @NonNull GlideRequests imageLoader,
                          boolean isMyProfile) {
        super(itemView);
        mImageLoader = imageLoader;
        itemView.setOnClickListener(this);
        if (isMyProfile || isAdmin()) {
            addPostDeleteOption(itemView);
        }
    }

    private boolean isAdmin() {
        String email = SessionManager.getInstance().getCurrentUser().getEmail();
        List<String> admins = RemoteConfig.getInstance().getAdmins();
        return admins.contains(email);
    }

    private void addPostDeleteOption(final View itemView) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(itemView.getContext())
                        .title("Delete post?")
                        .positiveText("DELETE")
                        .negativeText("CANCEL")
                        .positiveColorRes(R.color.red)
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
        vPostImage.display(post.getImage(), mImageLoader);
    }

    @Override
    public void resetState() {
        vPostImage.setImageDrawable(null);
    }

    @Override
    public void onClick(View v) {
        if (mPost != null) {
            PostPreviewActivity.startScreen(v.getContext(), mPost);
        }
    }
}
