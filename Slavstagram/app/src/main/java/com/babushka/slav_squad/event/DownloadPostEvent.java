package com.babushka.slav_squad.event;

import android.support.annotation.NonNull;

/**
 * Created by iliyan on 19.08.17.
 */

public class DownloadPostEvent {
    @NonNull
    private final String mImageUrl;

    public DownloadPostEvent(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @NonNull
    public String getImageUrl() {
        return mImageUrl;
    }
}
