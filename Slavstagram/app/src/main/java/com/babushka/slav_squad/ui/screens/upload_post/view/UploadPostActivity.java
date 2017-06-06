package com.babushka.slav_squad.ui.screens.upload_post.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.BaseActivity;
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

    @Nullable
    private MaterialDialog mProgressDialog;

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
            showToast("Camera app not found on device, please install one :)");
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
        showToast("Cyka, we need your camera to take pictures!");
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
    public void openGalleryWithCheck(int requestCode) {
        UploadPostActivityPermissionsDispatcher.openGalleryWithCheck(this, requestCode);
    }

    @SuppressLint("NewApi")
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void openGallery(int requestCode) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooserIntent, requestCode);
        } else {
            showToast("Gallery app not found on device, please install one :)");
        }
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForReadStorage(@NonNull PermissionRequest request) {
        //TODO: Implement method
        showToast("Cyka, we need your storage to pick up a picture!");
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showDeniedForReadStorage() {
        //TODO: Implement method
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showNeverAskForReadStorage() {
        //TODO: Implement method
    }


    @Override
    public void openImageCropScreen(@NonNull Uri sourceUri, @NonNull Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        //TODO: Customize cropper
        options.setFreeStyleCropEnabled(true);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ALL, UCropActivity.SCALE);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setMaxBitmapSize(1000);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    @Override
    public void setUploading() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title("Uploading post")
                .content("Please wait...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @Override
    public void showError(@NonNull String message) {
        showToast(message);
        dismissProgressDialog();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showSuccessAndCloseScreen() {
        showToast("Post uploaded");
        finish();
    }

    private void showToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.upload_post_camera_button)
    public void onCameraButtonClicked() {
        mPresenter.handleCameraClicked();
    }

    @OnClick(R.id.upload_post_gallery_button)
    public void onGalleryButtonClicked() {
        mPresenter.handleGalleryClicked();
    }

    @OnClick(R.id.upload_post_upload_button)
    public void onUploadButtonClicked() {
        String desc = vDescInput.getText().toString();
        mPresenter.handleUpload(desc);
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
