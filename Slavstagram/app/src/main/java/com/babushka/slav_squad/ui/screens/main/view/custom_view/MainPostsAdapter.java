package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.GlideRequests;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.screens.post.BasePostsAdapter;

/**
 * Created by iliyan on 07.06.17.
 */

class MainPostsAdapter extends BasePostsAdapter {
    MainPostsAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader) {
        super(context, imageLoader);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.adapter_main_post_view_holder;
    }
}
