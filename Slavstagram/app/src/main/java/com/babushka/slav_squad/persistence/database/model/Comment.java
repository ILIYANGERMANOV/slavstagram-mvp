package com.babushka.slav_squad.persistence.database.model;

import com.babushka.slav_squad.persistence.database.Table;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by iliyan on 28.05.17.
 */
@IgnoreExtraProperties
public class Comment {
    @PropertyName(Table.Comment.UID)
    private String mUid;
    @PropertyName(Table.Comment.TEXT)
    private String mText;
    @PropertyName(Table.Comment.AUTHOR_ID)
    private String mAuthorId;
    @PropertyName(Table.Comment.TIMESTAMP)
    private long mTimestamp;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }
}
