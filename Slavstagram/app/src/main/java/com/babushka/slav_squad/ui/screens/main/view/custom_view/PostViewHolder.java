package com.babushka.slav_squad.ui.screens.main.view.custom_view;

import android.support.annotation.NonNull;
import android.view.View;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.BaseAdapter;

/**
 * Created by iliyan on 29.05.17.
 */

class PostViewHolder extends BaseAdapter.BaseViewHolder<Post> {
    PostViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void display(@NonNull Post item) {
        //TODO: Implement method
    }

    @Override
    public void resetState() {
        //TODO: Implement method
    }
}
