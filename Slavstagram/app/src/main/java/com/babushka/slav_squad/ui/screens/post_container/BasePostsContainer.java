package com.babushka.slav_squad.ui.screens.post_container;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.container.BaseAdapter;
import com.babushka.slav_squad.ui.container.BaseContainer;

/**
 * Created by iliyan on 07.06.17.
 */

public abstract class BasePostsContainer<A extends BaseAdapter<Post, VH>, VH extends BaseAdapter.BaseViewHolder<Post>>
        extends BaseContainer<Post, VH, A> {
    public BasePostsContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onCustomSetup(@NonNull Activity activity) {
        ((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
    }
}