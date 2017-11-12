package com.babushka.slav_squad.session.data;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by iliyan on 16.06.17.
 */

public class UserDetails extends LoginDetails {
    private String mDisplayName;
    private String mPhotoUrl;

    public UserDetails(@NonNull String email, @NonNull String password) {
        super(email, password);
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(@NonNull String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    @Override
    public boolean validate() {
        return super.validate() && !TextUtils.isEmpty(mDisplayName);
    }

    @NonNull
    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }
}
