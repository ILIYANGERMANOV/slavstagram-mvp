package com.babushka.slav_squad.persistence.database.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.Table;
import com.babushka.slav_squad.ui.container.Findable;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iliyan on 28.05.17.
 */
@IgnoreExtraProperties
public class User implements Findable {
    @PropertyName(value = Table.User.UID)
    private String mUid;
    @PropertyName(value = Table.User.NOTIFICATION_TOKEN)
    private String mNotificationToken;
    @PropertyName(value = Table.User.EMAIL)
    private String mEmail;
    @PropertyName(value = Table.User.DISPLAY_NAME)
    private String mDisplayName;
    @PropertyName(value = Table.User.PHOTO_URL)
    private String mPhotoUrl;

    public User(@NonNull FirebaseUser user) {
        //TODO: handle missing attributes and make proper implementation
        mUid = user.getUid();
        mNotificationToken = FirebaseInstanceId.getInstance().getToken();
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

    @PropertyName(value = Table.User.UID)
    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    @Nullable
    @PropertyName(value = Table.User.NOTIFICATION_TOKEN)
    public String getNotificationToken() {
        return mNotificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        mNotificationToken = notificationToken;
    }

    @PropertyName(value = Table.User.EMAIL)
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    @Nullable
    @PropertyName(value = Table.User.DISPLAY_NAME)
    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    @Nullable
    @PropertyName(value = Table.User.PHOTO_URL)
    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toCreationMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Table.User.UID, mUid);
        result.put(Table.User.NOTIFICATION_TOKEN, mNotificationToken);
        result.put(Table.User.EMAIL, mEmail);
        result.put(Table.User.DISPLAY_NAME, mDisplayName);
        result.put(Table.User.PHOTO_URL, mPhotoUrl);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return mUid.equals(user.mUid) && mEmail.equals(user.mEmail);
    }

    @Override
    public int hashCode() {
        int result = mUid.hashCode();
        result = 31 * result + mEmail.hashCode();
        return result;
    }

    @NonNull
    @Override
    public String getId() {
        return mUid;
    }
}
