package com.babushka.slav_squad.persistence.database.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Table;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iliyan on 28.05.17.
 */
@IgnoreExtraProperties
public class User {
    @PropertyName(value = Table.User.EMAIL)
    private String mEmail;
    @PropertyName(value = Table.User.DISPLAY_NAME)
    private String mDisplayName;
    @PropertyName(value = Table.User.PHOTO_URL)
    private String mPhotoUrl;
    @PropertyName(value = Table.User.LIKED_POSTS_IDS)
    private Map<String, Boolean> mLikesPostsIds;

    public User(@NonNull FirebaseUser user) {
        //TODO: handle missing attributes and make proper implementation
        mEmail = user.getEmail();
        mDisplayName = user.getDisplayName();
        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null) {
            mPhotoUrl = photoUrl.toString();
        }
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Map<String, Boolean> getLikesPostsIds() {
        return mLikesPostsIds;
    }

    public void setLikesPostsIds(Map<String, Boolean> likesPostsIds) {
        mLikesPostsIds = likesPostsIds;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
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

    @Exclude
    public Map<String, Object> toCreationMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Table.User.EMAIL, mEmail);
        result.put(Table.User.DISPLAY_NAME, mDisplayName);
        result.put(Table.User.PHOTO_URL, mPhotoUrl);
        return result;
    }
}
