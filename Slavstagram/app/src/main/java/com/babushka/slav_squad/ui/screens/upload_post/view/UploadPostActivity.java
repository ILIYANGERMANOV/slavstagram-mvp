package com.babushka.slav_squad.ui.screens.upload_post.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.screens.BaseActivity;
import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;
import com.babushka.slav_squad.ui.screens.upload_post.model.UploadPostModel;
import com.babushka.slav_squad.ui.screens.upload_post.presenter.UploadPostPresenter;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class UploadPostActivity extends BaseActivity<UploadPostPresenter>
        implements UploadPostContract.View {
    @BindView(R.id.upload_post_image_view)
    ImageView vImage;
    @BindView(R.id.upload_post_desc_edit_text)
    EditText vDescInput;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, UploadPostActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_upload_post;
    }

    @NonNull
    @Override
    protected UploadPostPresenter initializePresenter() {
        return new UploadPostPresenter(this, new UploadPostModel(this));
    }

    @Override
    public void displayPostImage(@NonNull Uri photoUri) {
        GlideApp.with(this)
                .load(photoUri)
                .into(vImage);
    }

    @Override
    public void openCameraWithPermissionCheck(int requestCode) {
        UploadPostActivityPermissionsDispatcher.openCameraWithCheck(this, requestCode);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void openCamera(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            createPhotoFileAndStartCameraApp(takePictureIntent, requestCode);
        } else {
            Toast.makeText(this, "Camera app not found on device, please install one :)", Toast.LENGTH_LONG).show();
        }
    }

    private void createPhotoFileAndStartCameraApp(Intent takePictureIntent, int requestCode) {
        try {
            File photoFile = mPresenter.providePhotoFile();
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.babushka.slav_squad.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, requestCode);
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(@NonNull PermissionRequest request) {
        //TODO: Implement method
        Toast.makeText(this, "Cyka, we need your camera to take pictures!", Toast.LENGTH_LONG).show();
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showDeniedForCamera() {
        //TODO: Implement method
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void showNeverAskForCamera() {
        //TODO: Implement method
    }

    @Override
    public void openGallery(int requestCode) {
        //TODO: Implement method
    }

    @Override
    public void openImageCropScreen(@NonNull Uri sourceUri, @NonNull Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        //TODO: Customize cropper
        options.setFreeStyleCropEnabled(true);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ALL, UCropActivity.SCALE);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    @Override
    public void showError(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.upload_post_camera_button)
    public void onCameraButtonClicked() {
        mPresenter.handleCameraClicked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UploadPostActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
