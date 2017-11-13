package com.babushka.slav_squad.persistence.database.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.Table;
import com.babushka.slav_squad.ui.container.Findable;
import com.facebook.AccessToken;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iliyan on 28.05.17.
 */
@IgnoreExtraProperties
public class User implements Findable {
    private static final String HIGH_RES_IMAGE_SIZE = "400";
    private static final String GOOGLE_PROVIDER_ID = "google.com";
    private static final String REGISTER_ACCOUNT_PROVIDER = "password";
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
    @PropertyName(value = Table.User.HIGH_RES_PHOTO_URL)
    private String mHighResPhotoUrl;

    public User(@NonNull FirebaseUser currentUser) {
        mUid = currentUser.getUid();
        mNotificationToken = FirebaseInstanceId.getInstance().getToken();
        mEmail = currentUser.getEmail();
        mDisplayName = currentUser.getDisplayName();
        Uri photoUrl = currentUser.getPhotoUrl();
        if (photoUrl != null) {
            mPhotoUrl = photoUrl.toString();
        }
        mHighResPhotoUrl = getHighResProfilePicture(currentUser.getProviders(), mPhotoUrl);

    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @NonNull
    private static String getHighResProfilePicture(@Nullable List<String> providers, @NonNull String photoUrl) {
        if (providers == null || providers.isEmpty()) {
            return photoUrl;
        }
        String provider = providers.get(0);
        switch (provider) {
            case FacebookAuthProvider.PROVIDER_ID:
                String facebookUserId = AccessToken.getCurrentAccessToken().getUserId();
                return getFbHighResProfilePicture(facebookUserId);
            case GOOGLE_PROVIDER_ID:
                return getGoogleHighResProfilePicture(photoUrl);
            case REGISTER_ACCOUNT_PROVIDER:
                return photoUrl;
            default:
                throw new RuntimeException(String.format("Unknown provider - '%s'", provider));
        }
    }

    @NonNull
    private static String getFbHighResProfilePicture(@NonNull String facebookUserId) {
        return "https://graph.facebook.com/" + facebookUserId +
                "/picture?width=" + HIGH_RES_IMAGE_SIZE + "&height=" + HIGH_RES_IMAGE_SIZE;
    }

    @NonNull
    private static String getGoogleHighResProfilePicture(@NonNull String firebasePhotoUrl) {
        return firebasePhotoUrl.replace("/s96-c/", "/s" + HIGH_RES_IMAGE_SIZE + "-c/");
    }

    @PropertyName(value = Table.User.HIGH_RES_PHOTO_URL)
    public String getHighResPhotoUrl() {
        return mHighResPhotoUrl;
    }

    public void setHighResPhotoUrl(@Nullable String highResPhotoUrl) {
        mHighResPhotoUrl = highResPhotoUrl;
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
        result.put(Table.User.HIGH_RES_PHOTO_URL, mHighResPhotoUrl);
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
