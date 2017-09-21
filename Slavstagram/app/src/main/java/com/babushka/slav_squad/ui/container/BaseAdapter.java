package com.babushka.slav_squad.ui.container;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by iliyan on 29.05.17.
 */

public abstract class BaseAdapter<T extends Findable, VH extends BaseAdapter.BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

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

    protected List<T> buildDataList() {
        return new LinkedList<>();
    }

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

    public void add(int position, @NonNull T item) {
        ensureDataListIsInitialized();
        mData.add(position, item);
        notifyItemInserted(position);
    }

    void display(@NonNull List<T> data) {
        if (mData != null) {
            mData.clear();
        } else {
            mData = buildDataList();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    void add(@NonNull T item) {
        ensureDataListIsInitialized();
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
    }

    void update(@NonNull T item) {
        ensureDataListIsInitialized();
        try {
            int itemPosition = findItemPosition(item);
            mData.set(itemPosition, item);
            notifyItemChanged(itemPosition);
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    void remove(@NonNull T item) {
        ensureDataListIsInitialized();
        try {
            int itemPosition = findItemPosition(item);
            mData.remove(itemPosition);
            notifyItemRemoved(itemPosition);
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int findItemPosition(@NonNull T desiredItem) throws ItemNotFoundException {
        String desiredItemId = desiredItem.getId();
        for (int i = 0; i < mData.size(); ++i) {
            T item = mData.get(i);
            if (item.getId().equals(desiredItemId)) {
                return i;
            }
        }
        throw new ItemNotFoundException();
    }


    private void ensureDataListIsInitialized() {
        if (mData == null) {
            mData = buildDataList();
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }


    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public abstract void display(@NonNull T item);

        public abstract void resetState();
    }

    private static class ItemNotFoundException extends Exception {
    }
}
