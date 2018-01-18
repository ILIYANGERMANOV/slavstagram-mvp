package com.babushka.slav_squad.persistence.database.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.Table;
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
public class User extends UserBase {
    private static final String HIGH_RES_IMAGE_SIZE = "400";
    private static final String GOOGLE_PROVIDER_ID = "google.com";
    private static final String REGISTER_ACCOUNT_PROVIDER = "password";

    @PropertyName(value = Table.User.NOTIFICATION_TOKEN)
    private String mNotificationToken;
    @PropertyName(value = Table.User.EMAIL)
    private String mEmail;
    @PropertyName(value = Table.User.HIGH_RES_PHOTO_URL)
    private String mHighResPhotoUrl;

    public User(@NonNull FirebaseUser firebaseUser) {
        super(firebaseUser);
        mNotificationToken = FirebaseInstanceId.getInstance().getToken();
        mEmail = firebaseUser.getEmail();
        mHighResPhotoUrl = getHighResProfilePicture(firebaseUser.getProviders(), mPhotoUrl);

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
                AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
                if (fbAccessToken != null) {
                    String facebookUserId = fbAccessToken.getUserId();
                    return getFbHighResProfilePicture(facebookUserId);
                } else {
                    return photoUrl;
                }
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

    @Exclude
    public Map<String, Object> toCreationMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Table.User.UID, mUid);
        result.put(Table.User.DISPLAY_NAME, mDisplayName);
        result.put(Table.User.PHOTO_URL, mPhotoUrl);
        result.put(Table.User.NOTIFICATION_TOKEN, mNotificationToken);
        result.put(Table.User.EMAIL, mEmail);
        result.put(Table.User.HIGH_RES_PHOTO_URL, mHighResPhotoUrl);
        return result;
    }
}
