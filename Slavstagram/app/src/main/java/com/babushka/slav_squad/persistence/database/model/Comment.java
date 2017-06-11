package com.babushka.slav_squad.persistence.database.model;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Table;
import com.babushka.slav_squad.ui.container.Findable;
import com.babushka.slav_squad.util.datetime.DateUtil;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by iliyan on 28.05.17.
 */
@IgnoreExtraProperties
public class Comment implements Findable {
    @PropertyName(Table.Comment.UID)
    private String mUid;
    @PropertyName(Table.Comment.TEXT)
    private String mText;
    @PropertyName(Table.Comment.AUTHOR)
    private User mAuthor;
    @PropertyName(Table.Comment.TIMESTAMP)
    private long mTimestamp;
    @PropertyName(Table.Comment.INVERTED_TIMESTAMP)
    private long mInvertedTimestamp;

    public Comment(@NonNull User author, @NonNull String text) {
        mAuthor = author;
        mText = text;
        mTimestamp = DateUtil.getTimestampForNow();
        mInvertedTimestamp = -1 * mTimestamp;
    }

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @PropertyName(Table.Comment.UID)
    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    @PropertyName(Table.Comment.TEXT)
    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }


    @PropertyName(Table.Comment.AUTHOR)
    public User getAuthor() {
        return mAuthor;
    }

    public void setAuthor(User author) {
        mAuthor = author;
    }

    @PropertyName(Table.Comment.TIMESTAMP)
    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }


    @PropertyName(Table.Comment.INVERTED_TIMESTAMP)
    public long getInvertedTimestamp() {
        return mInvertedTimestamp;
    }

    public void setInvertedTimestamp(long invertedTimestamp) {
        mInvertedTimestamp = invertedTimestamp;
    }

    @NonNull
    @Override
    public String getId() {
        return mUid;
    }
}
