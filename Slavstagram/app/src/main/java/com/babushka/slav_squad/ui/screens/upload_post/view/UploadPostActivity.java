package com.babushka.slav_squad.ui.screens.upload_post.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.custom_view.AspectRatioImageView;
import com.babushka.slav_squad.ui.dialog.PermissionDenyDialog;
import com.babushka.slav_squad.ui.dialog.PermissionNeverAskDialog;
import com.babushka.slav_squad.ui.dialog.PermissionRationaleDialog;
import com.babushka.slav_squad.ui.screens.cropping.CropHandler;
import com.babushka.slav_squad.ui.screens.cropping.ImageCropper;
import com.babushka.slav_squad.ui.screens.cropping.UploadPostCropper;
import com.babushka.slav_squad.ui.screens.image_preview.ImagePreviewActivity;
import com.babushka.slav_squad.ui.screens.upload_post.UploadPostContract;
import com.babushka.slav_squad.ui.screens.upload_post.model.UploadPostModel;
import com.babushka.slav_squad.ui.screens.upload_post.presenter.UploadPostPresenter;
import com.babushka.slav_squad.util.IntentBuilder;
import com.wonderkiln.blurkit.BlurLayout;

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

    @BindView(R.id.upload_post_background_image_view)
    ImageView vBackgroundImage;
    @BindView(R.id.upload_post_blur_layout)
    BlurLayout vBlurLayout;
    @BindView(R.id.upload_post_aspect_ratio_image_view)
    AspectRatioImageView vPostImage;
    @BindView(R.id.upload_post_desc_edit_text)
    EditText vDescInput;

    @Nullable
    private MaterialDialog mProgressDialog;

    private boolean mUseCamera;
    private Uri mSelectedImage;

    @Nullable
    private Uri mDisplayedPhoto;

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
    protected void onSetupUI() {
        super.onSetupUI();
        setActionBarTitle("");
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.applyBusinessLogic(mSelectedImage, mUseCamera);
    }

    @Override
    public void displayPostImage(@NonNull Uri photoUri) {
        mDisplayedPhoto = photoUri;
//        GlideApp.with(this)
//                .load(photoUri)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
//                                                Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
//                                                   DataSource dataSource, boolean isFirstResource) {
//                        vBlurLayout.invalidate();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                vBlurLayout.invalidate();
//                            }
//                        }, 50);
//                        return false;
//                    }
//                })
//                .into(vBackgroundImage);
        vPostImage.display(Storage.newPostImage(photoUri),
                GlideApp.with(this));
        vBlurLayout.bringToFront();
    }

    @OnClick(R.id.upload_post_aspect_ratio_image_view)
    public void onPostImageClick() {
        if (mDisplayedPhoto != null) {
            ImagePreviewActivity.startScreen(this, mDisplayedPhoto.getPath());
        }
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
        dismissProgressDialogIfShown();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_upload_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload_post:
                String desc = vDescInput.getText().toString();
                mPresenter.handleUpload(desc);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    protected String getScreenName() {
        return EventValues.Screen.UPLOAD_POST;
    }
}
