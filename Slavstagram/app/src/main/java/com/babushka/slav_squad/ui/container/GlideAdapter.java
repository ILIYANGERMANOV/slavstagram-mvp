package com.babushka.slav_squad.ui.container;

import android.content.Context;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.GlideRequests;

/**
 * Created by iliyan on 07.06.17.
 */

public abstract class GlideAdapter<T extends Findable, VH extends BaseAdapter.BaseViewHolder<T>> extends BaseAdapter<T, VH> {
    @NonNull
    protected final GlideRequests mImageLoader;

    protected GlideAdapter(@NonNull Context context, @NonNull GlideRequests imageLoader) {
        super(context);
        mImageLoader = imageLoader;
    }
}
