package com.babushka.slav_squad.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by iliyan on 29.05.17.
 */

public abstract class BaseContainer<T, VH extends BaseAdapter.BaseViewHolder<T>, A extends BaseAdapter<T, VH>> extends RecyclerView {
    protected A mAdapter;
    protected LayoutManager mLayoutManager;

    public BaseContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract A initializeAdapter(@NonNull Activity activity);

    public void setup(@NonNull Activity activity) {
        setAdapter(initializeAdapter(activity));
        mLayoutManager = new LinearLayoutManager(activity);
        setLayoutManager(mLayoutManager);
    }

    public void display(@NonNull List<T> data) {
        if (mAdapter == null) {
            throw new IllegalStateException("BaseContainer#setup() must be called before displaying data");
        }
        mAdapter.display(data);
    }
}
