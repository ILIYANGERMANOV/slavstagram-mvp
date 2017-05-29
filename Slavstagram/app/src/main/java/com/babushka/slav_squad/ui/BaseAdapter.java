package com.babushka.slav_squad.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iliyan on 29.05.17.
 */

public abstract class BaseAdapter<T, VH extends BaseAdapter.BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

    protected final Context mContext;

    @NonNull
    protected final LayoutInflater mInflater;

    protected List<T> mData;

    protected BaseAdapter(@NonNull Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @LayoutRes
    protected abstract int getItemLayout();

    protected abstract VH initializeViewHolder(@NonNull View layoutView);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = mInflater.inflate(getItemLayout(), parent, false);
        return initializeViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.resetState();
        holder.display(mData.get(position));
    }

    public void display(@NonNull List<T> data) {
        if (mData != null) {
            mData.clear();
        } else {
            mData = new ArrayList<>();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void display(@NonNull T item);

        public abstract void resetState();
    }
}
