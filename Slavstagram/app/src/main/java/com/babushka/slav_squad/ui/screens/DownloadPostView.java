package com.babushka.slav_squad.ui.screens;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by iliyan on 29.12.17.
 */

public interface DownloadPostView {
    void downloadPostWithPermissionCheck(@NonNull String imageUrl);

    void showDownloadPostLoading();

    void showDownloadPostSuccess();

    void showDownloadPostError();

    void addImageToGallery(@NonNull File imageFile);
}
