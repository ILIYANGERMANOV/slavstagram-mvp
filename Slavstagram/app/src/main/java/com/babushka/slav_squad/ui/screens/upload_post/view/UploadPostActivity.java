package com.babushka.slav_squad.ui.screens.upload_post.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.dialog.PermissionDenyDialog;
import com.babushka.slav_squad.ui.dialog.PermissionNeverAskDialog;
import com.babushka.slav_squad.ui.dialog.PermissionRationaleDialog;
import com.babushka.slav_squad.ui.screens.cropping.CropHandler;
import com.babushka.slav_squad.ui.screens.cropping.ImageCropper;
import com.babushka.slav_squad.ui.screens.cropping.UploadPostCropper;
import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;
import com.babushka.slav_squad.ui.screens.upload_post.model.UploadPostModel;
import com.babushka.slav_squad.ui.screens.upload_post.presenter.UploadPostPresenter;
import com.babushka.slav_squad.util.IntentBuilder;

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
public class UploadPostActivity extends BaseActionBarActivity<UploadPostContract.Presenter>
        implements UploadPostContract.View {
    private static final String EXTRA_USE_CAMERA = "use_camera_extra";
    private static final String EXTRA_SELECTED_IMAGE_URI = "selected_image_uri_extra";

    @BindView(R.id.upload_post_image_view)
    ImageView vImage;
    @BindView(R.id.upload_post_desc_edit_text)
    EditText vDescInput;

    @Nullable
    private MaterialDialog mProgressDialog;

    private boolean mUseCamera;
    private Uri mSelectedImage;

    public static void startScreenForResult(@NonNull Activity activity, int requestCode,
                                            @NonNull Uri selectedImage) {
        startScreenForResult(activity, requestCode, selectedImage, false);
    }

    public static void startScreenForResult(@NonNull Activity activity, int requestCode,
                                            boolean useCamera) {
        startScreenForResult(activity, requestCode, null, useCamera);
    }

    private static void startScreenForResult(@NonNull Activity activity, int requestCode,
                                             @Nullable Uri selectedImage, boolean useCamera) {
        Intent intent = new Intent(activity, UploadPostActivity.class);
        intent.putExtra(EXTRA_USE_CAMERA, useCamera);
        intent.putExtra(EXTRA_SELECTED_IMAGE_URI, selectedImage);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_upload_post;
    }

    @NonNull
    @Override
    protected UploadPostContract.Presenter initializePresenter() {
        return new UploadPostPresenter(this, new UploadPostModel(this), new CropHandler(this));
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mSelectedImage = intent.getParcelableExtra(EXTRA_SELECTED_IMAGE_URI);
        mUseCamera = intent.getBooleanExtra(EXTRA_USE_CAMERA, false);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.applyBusinessLogic(mSelectedImage, mUseCamera);
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
        try {
            File photoFile = mPresenter.providePhotoFile();
            Intent takePictureIntent = IntentBuilder.buildOpenCameraIntent(this, photoFile);
            startActivityForResult(takePictureIntent, requestCode);
        } catch (IntentBuilder.ResolveActivityException e) {
            showToast("Camera app not found on device, please install one :)");
        } catch (IOException e) {
            showToast("IOException while creating photo file");
        }
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(@NonNull PermissionRequest request) {
        new PermissionRationaleDialog(getString(R.string.permission_camera_rationale_title),
                getString(R.string.permission_camera_rationale_content), request).show(this);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void showDeniedForCamera() {
        new PermissionDenyDialog(getString(R.string.permission_camera_deny_title),
                getString(R.string.permission_camera_deny_content)).show(this);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void showNeverAskForCamera() {
        new PermissionNeverAskDialog(getString(R.string.permission_camera_never_ask_title),
                getString(R.string.permission_camera_never_ask_content)).show(this);
    }


    @Override
    public void openGalleryWithCheck(int requestCode) {
        UploadPostActivityPermissionsDispatcher.openGalleryWithCheck(this, requestCode);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void openGallery(int requestCode) {
        try {
            Intent intent = IntentBuilder.buildOpenGalleryWithChooserIntent(this);
            startActivityForResult(intent, requestCode);
        } catch (IntentBuilder.ResolveActivityException e) {
            showToast("Gallery app not found on device, please install one :)");
        }
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForReadStorage(@NonNull PermissionRequest request) {
        new PermissionRationaleDialog(getString(R.string.permission_read_storage_rationale_title),
                getString(R.string.permission_read_storage_rationale_content), request).show(this);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showDeniedForReadStorage() {
        new PermissionDenyDialog(getString(R.string.permission_read_storage_deny_title),
                getString(R.string.permission_read_storage_deny_content)).show(this);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showNeverAskForReadStorage() {
        new PermissionNeverAskDialog(getString(R.string.permission_read_storage_never_ask_title),
                getString(R.string.permission_read_storage_never_ask_content)).show(this);
    }


    @Override
    public void openImageCropScreen(@NonNull Uri sourceUri, @NonNull Uri destinationUri) {
        ImageCropper cropper = new UploadPostCropper(this);
        cropper.crop(this, sourceUri, destinationUri);
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
        dismissProgressDialogIfShown();
        showToast(message);
    }

    @Override
    public void showSuccessAndCloseScreen() {
        dismissProgressDialogIfShown();
        showToast("Post uploaded");
        setResult(RESULT_OK);
        closeScreen();
    }

    @Override
    public void closeScreen() {
        finish();
    }

    private void dismissProgressDialogIfShown() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialogIfShown();
    }
}
