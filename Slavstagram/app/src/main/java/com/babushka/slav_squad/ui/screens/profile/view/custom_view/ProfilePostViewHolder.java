package com.babushka.slav_squad.ui.screens.profile.view.custom_view;

import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.screens.post_container.PostView;
import com.babushka.slav_squad.ui.screens.post_container.PostViewHolder;

/**
 * Created by iliyan on 08.06.17.
 */

public class ProfilePostViewHolder extends PostViewHolder {
    ProfilePostViewHolder(View itemView, @NonNull GlideRequests imageLoader,
                          boolean isMyProfile) {
        super(itemView, imageLoader);
        if (isMyProfile) {
            addPostDeleteOption(itemView);
        }
    }

    private void addPostDeleteOption(final View itemView) {
        mPostView.setOnPostLongClickListener(new PostView.OnPostLongClickListener() {
            @Override
            public void onPostLongClick(@NonNull final Post post) {
                new MaterialDialog.Builder(itemView.getContext())
                        .title("Delete post?")
                        .positiveText("DELETE")
                        .negativeText("CANCEL")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Database.getInstance().deletePost(post);
                            }
                        }).show();
            }
        });
    }
}
