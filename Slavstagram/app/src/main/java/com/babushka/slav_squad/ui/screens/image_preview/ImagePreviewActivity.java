package com.babushka.slav_squad.ui.screens.image_preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iliyan on 11.06.17.
 */

public class ImagePreviewActivity extends AppCompatActivity {
    private static final String EXTRA_IMAGE_URL = "image_url_extra";

    @BindView(R.id.image_preview_photo_view)
    PhotoView mPhotoView;

    private String mImageUrl;

    public static void startScreen(@NonNull Context context, @NonNull String imageUrl) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readArguments();
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);
        setupActionBar();
        loadImage();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadImage() {
        GlideApp.with(this)
                .load(mImageUrl)
                //TODO: add placeholder and error drawable
                .into(mPhotoView);
    }

    private void readArguments() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        if (mImageUrl == null) {
            throw new IllegalArgumentException("ImagePreviewActivity: '" + EXTRA_IMAGE_URL + "' is required!");
        }
    }
}
