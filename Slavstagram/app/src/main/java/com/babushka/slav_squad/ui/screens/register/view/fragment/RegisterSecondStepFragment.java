package com.babushka.slav_squad.ui.screens.register.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.cropping.CropHandler;
import com.babushka.slav_squad.ui.screens.cropping.ProfilePictureCropper;
import com.babushka.slav_squad.ui.screens.register.view.RegisterSupport;
import com.babushka.slav_squad.ui.wizard.WizardFragment;
import com.babushka.slav_squad.util.IntentBuilder;
import com.yalantis.ucrop.UCrop;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by iliyan on 16.06.17.
 */
@RuntimePermissions
public class RegisterSecondStepFragment extends WizardFragment<Uri, RegisterSupport> {
    private static final int RC_OPEN_GALLERY = 1;

    @BindView(R.id.register_second_step_circle_image_view)
    CircleImageView vCircleImage;

    @Nullable
    private Uri mCroppedPhoto;
    @Nullable
    private CropHandler mCropHandler;

    public static RegisterSecondStepFragment newInstance() {
        return new RegisterSecondStepFragment();
    }

    @Override
    protected void onNext(@NonNull RegisterSupport support, Uri input) {
        support.onProfilePhotoSelected(input);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_second_step;
    }

    @OnClick(R.id.register_second_step_gallery_button)
    public void onGalleryButtonClicked() {
        RegisterSecondStepFragmentPermissionsDispatcher.openGalleryWithCheck(this, RC_OPEN_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        RegisterSecondStepFragmentPermissionsDispatcher.onRequestPermissionsResult(this,
                requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void openGallery(int requestCode) {
        try {
            Intent intent = IntentBuilder.buildOpenGalleryIntent(getContext());
            startActivityForResult(intent, requestCode);
        } catch (IntentBuilder.ResolveActivityException ignored) {
        }
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForReadStorage(@NonNull PermissionRequest request) {
        //TODO: Implement method
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_OPEN_GALLERY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    GalleryResult galleryResult = new GalleryResult(getContext(), data);
                    try {
                        Uri selectedImageFromGallery = galleryResult.getSelectedImageFromGallery();
                        cropPhoto(selectedImageFromGallery);
                    } catch (GalleryResult.SelectedImageNotFoundException ignored) {
                        //TODO: Handle gallery result excep+tion
                    }
                }
                break;
            case UCrop.REQUEST_CROP:
                if (mCropHandler != null) {
                    mCropHandler.handleCropResult(resultCode, data);
                }
                break;

        }

    }

    private void cropPhoto(Uri selectedImageFromGallery) {
        mCropHandler = new CropHandler(getContext());
        mCropHandler.cropPhoto(selectedImageFromGallery, new CropHandler.Result() {
            @Override
            public void onStartCropActivity(@NonNull Uri sourceUri, @NonNull Uri destinationUri) {
                ProfilePictureCropper cropper = new ProfilePictureCropper(getContext());
                cropper.crop(RegisterSecondStepFragment.this, sourceUri, destinationUri);
            }

            @Override
            public void onCropped(@NonNull Uri croppedImage) {
                mCroppedPhoto = croppedImage;
                displayPhoto();
            }

            @Override
            public void onError(@NonNull String message) {
                Toast.makeText(getContext(), "Error while while cropping image: " + message,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteCurrentPhotoFile() {
                //nothing to do here
            }
        });
    }

    private void displayPhoto() {
        GlideApp.with(this)
                .load(mCroppedPhoto)
                .dontAnimate()
                .into(vCircleImage);
    }

    @OnClick(R.id.register_second_step_register_button)
    public void onRegisterButtonClicked() {
        next(mCroppedPhoto);
    }
}
