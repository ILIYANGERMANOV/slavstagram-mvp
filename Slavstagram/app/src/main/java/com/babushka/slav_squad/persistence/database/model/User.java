package com.babushka.slav_squad.persistence.database.model;

import com.babushka.slav_squad.persistence.database.Table;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.Map;

/**
 * Created by iliyan on 28.05.17.
 */
@IgnoreExtraProperties
public class User {
    @PropertyName(value = Table.User.DISPLAY_NAME)
    private String mDisplayName;
    @PropertyName(value = Table.User.PHOTO_URL)
    private String mPhotoUrl;
    @PropertyName(value = Table.User.LIKED_POSTS)
    private Map<String, Boolean> mLikedPosts;

    public User(String displayName, String photoUrl, Map<String, Boolean> likedPosts) {
        mDisplayName = displayName;
        mPhotoUrl = photoUrl;
        mLikedPosts = likedPosts;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public Map<String, Boolean> getLikedPosts() {
        return mLikedPosts;
    }

    public void setLikedPosts(Map<String, Boolean> likedPosts) {
        mLikedPosts = likedPosts;
    }
}
