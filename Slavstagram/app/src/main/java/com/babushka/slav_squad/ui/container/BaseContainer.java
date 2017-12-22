package com.babushka.slav_squad.ui.container;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.GlideRequests;

import java.util.List;

/**
 * Created by iliyan on 29.05.17.
 */

public abstract class BaseContainer<T extends Findable, VH extends BaseAdapter.BaseViewHolder<T>, A extends BaseAdapter<T, VH>> extends RecyclerView {
    protected A mAdapter;
    protected LayoutManager mLayoutManager;

    public BaseContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract A initializeAdapter(@NonNull Activity activity, @NonNull GlideRequests imageLoader);

    public void setup(@NonNull Activity activity) {
        mAdapter = initializeAdapter(activity, GlideApp.with(activity));
        setAdapter(mAdapter);
        mLayoutManager = buildLayoutManager(activity);
        setLayoutManager(mLayoutManager);
        onCustomSetup(activity);
    }

    @NonNull
    protected LayoutManager buildLayoutManager(@NonNull Activity activity) {
        return new LinearLayoutManager(activity);
    }

    protected void onCustomSetup(@NonNull Activity activity) {
        //empty method
    }

    public void display(@NonNull List<T> data) {
        ensureAdapterIsReady();
        mAdapter.display(data);
    }

    public void add(int position, @NonNull T item) {
        ensureAdapterIsReady();
        mAdapter.add(position, item);
    }

    public void add(@NonNull T item) {
        ensureAdapterIsReady();
        mAdapter.add(item);
    }

    public void update(@NonNull T item) {
        ensureAdapterIsReady();
        mAdapter.update(item);
    }

    public void remove(@NonNull T item) {
        ensureAdapterIsReady();
        mAdapter.remove(item);
    }

    public void removeAll() {
        ensureAdapterIsReady();
        mAdapter.removeAll();
    }

    private void ensureAdapterIsReady() {
        if (mAdapter == null) {
            throw new IllegalStateException("BaseContainer#setup() must be called before displaying data");
        }
    }
}
