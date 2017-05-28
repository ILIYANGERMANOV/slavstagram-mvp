package com.babushka.slav_squad.persistence.database.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.Table;
import com.babushka.slav_squad.util.DateUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iliyan on 25.05.17.
 */
@IgnoreExtraProperties
public class Post {
    @PropertyName(value = Table.Post.UID)
    private String mUid;
    @PropertyName(value = Table.Post.AUTHOR_ID)
    private String mAuthorId;
    @PropertyName(value = Table.Post.DESCRIPTION)
    private String mDescription;
    @PropertyName(value = Table.Post.IMAGE_URL)
    private String mImageUrl;
    @PropertyName(value = Table.Post.LIKES_COUNT)
    private int mLikesCount;
    @PropertyName(value = Table.Post.LIKES)
    private Map<String, Boolean> mLikes;
    @PropertyName(value = Table.Post.COMMENTS)
    private Map<String, Boolean> mComments;
    @PropertyName(value = Table.Post.TIMESTAMP)
    private long mTimestamp;

    public Post(@NonNull String authorId, @Nullable String description,
                @NonNull String imageUrl) {
        mAuthorId = authorId;
        mDescription = description;
        mImageUrl = imageUrl;
        mLikesCount = 0;
        mLikes = new HashMap<>();
        mComments = new HashMap<>();
        mTimestamp = DateUtil.getTimestampForNow();
    }

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public void setLikesCount(int likesCount) {
        mLikesCount = likesCount;
    }

    @Nullable
    public Map<String, Boolean> getLikes() {
        return mLikes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        mLikes = likes;
    }

    @Nullable
    public Map<String, Boolean> getComments() {
        return mComments;
    }

    public void setComments(Map<String, Boolean> comments) {
        mComments = comments;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        mTimestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Table.Post.UID, mUid);
        result.put(Table.Post.AUTHOR_ID, mAuthorId);
        result.put(Table.Post.DESCRIPTION, mDescription);
        result.put(Table.Post.IMAGE_URL, mImageUrl);
        result.put(Table.Post.LIKES_COUNT, mLikesCount);
        result.put(Table.Post.LIKES, mLikes);
        result.put(Table.Post.COMMENTS, mComments);
        result.put(Table.Post.TIMESTAMP, mTimestamp);
        return result;
    }
}
