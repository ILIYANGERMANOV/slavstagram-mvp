package com.babushka.slav_squad.ui.screens.landing.register.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.landing.register.view.RegisterSupport;
import com.babushka.slav_squad.ui.wizard.WizardFragment;
import com.babushka.slav_squad.util.IntentBuilder;

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
    private Uri mPhotoUri;

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
        if (requestCode == RC_OPEN_GALLERY && resultCode == Activity.RESULT_OK
                && data != null) {
            GalleryResult galleryResult = new GalleryResult(getContext(), data);
            try {
                mPhotoUri = galleryResult.getSelectedImageFromGallery();
                displayPhoto();
            } catch (GalleryResult.SelectedImageNotFoundException ignored) {
                //TODO: Handle gallery result exception
            }
        }
    }

    private void displayPhoto() {
        GlideApp.with(this)
                .load(mPhotoUri)
                .dontAnimate()
                .into(vCircleImage);
    }

    @OnClick(R.id.register_second_step_register_button)
    public void onRegisterButtonClicked() {
        next(mPhotoUri);
    }
}
