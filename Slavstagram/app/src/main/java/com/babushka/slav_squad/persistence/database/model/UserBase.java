package com.babushka.slav_squad.persistence.database.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Table;
import com.babushka.slav_squad.ui.container.Findable;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iliyan on 10.12.17.
 */

public class UserBase implements Findable {
    @PropertyName(value = Table.User.UID)
    protected String mUid;
    @PropertyName(value = Table.User.DISPLAY_NAME)
    protected String mDisplayName;
    @PropertyName(value = Table.User.PHOTO_URL)
    protected String mPhotoUrl;

    public UserBase(@NonNull FirebaseUser firebaseUser) {
        mUid = firebaseUser.getUid();
        mDisplayName = firebaseUser.getDisplayName();
        Uri photoUrl = firebaseUser.getPhotoUrl();
        if (photoUrl != null) {
            mPhotoUrl = photoUrl.toString();
        }
    }

    public UserBase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @PropertyName(value = Table.User.UID)
    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    @PropertyName(value = Table.User.DISPLAY_NAME)
    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    @PropertyName(value = Table.User.PHOTO_URL)
    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toUserBaseMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Table.User.UID, mUid);
        result.put(Table.User.DISPLAY_NAME, mDisplayName);
        result.put(Table.User.PHOTO_URL, mPhotoUrl);
        return result;
    }


    @NonNull
    @Override
    public String getId() {
        return mUid;
    }
}
