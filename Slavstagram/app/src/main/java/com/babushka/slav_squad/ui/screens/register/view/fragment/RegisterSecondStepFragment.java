package com.babushka.slav_squad.ui.screens.register.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventBuilder;
import com.babushka.slav_squad.analytics.event.EventParamKeys;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.ui.custom_view.VolumeButton;
import com.babushka.slav_squad.ui.dialog.PermissionDenyDialog;
import com.babushka.slav_squad.ui.dialog.PermissionNeverAskDialog;
import com.babushka.slav_squad.ui.dialog.PermissionRationaleDialog;
import com.babushka.slav_squad.ui.listeners.editor.EditorGoListener;
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
public class RegisterSecondStepFragment extends WizardFragment<RegisterSecondStepFragment.Input, RegisterSupport> {
    private static final int RC_OPEN_GALLERY = 1;

    @BindView(R.id.register_second_step_volume_button)
    VolumeButton vVolumeButton;
    @BindView(R.id.register_second_step_circle_image_view)
    CircleImageView vCircleImage;
    @BindView(R.id.register_second_step_display_name_edit_text)
    EditText vDisplayNameInput;
    @BindView(R.id.register_second_step_register_button)
    Button vRegisterButton;

    @Nullable
    private Uri mCroppedPhoto;
    @Nullable
    private CropHandler mCropHandler;

    public static RegisterSecondStepFragment newInstance() {
        return new RegisterSecondStepFragment();
    }

    @Override
    protected void onNext(@NonNull RegisterSupport support, Input input) {
        support.onSecondStepCompleted(input);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_second_step;
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vDisplayNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vRegisterButton.setEnabled(validateInput());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        vDisplayNameInput.setOnEditorActionListener(new EditorGoListener() {
            @Override
            protected boolean onAction() {
                if (validateInput()) {
                    next(new Input(mCroppedPhoto, vDisplayNameInput.getText().toString()));
                    return true;
                }
                return false;
            }
        });
        vVolumeButton.setFromScreen(getScreenName());
        if (mCroppedPhoto != null) {
            displayPhoto();
        }
    }

    @Override
    protected void onInitialized() {
        logSimpleEvent(Events.OPEN_SCREEN_ + getScreenName());
    }

    private boolean validateInput() {
        return !TextUtils.isEmpty(vDisplayNameInput.getText());
    }

    @OnClick(R.id.register_second_step_circle_image_view)
    public void onSelectProfilePicture() {
        logSimpleEvent(Events.Register.REG_WITH_EMAIL_SELECT_IMAGE);
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
            logEventWithFromScreen(Events.Permission.READ_STORAGE_GRANTED);
            Intent intent = IntentBuilder.buildOpenGalleryWithChooserIntent(getContext());
            startActivityForResult(intent, requestCode);
        } catch (IntentBuilder.ResolveActivityException ignored) {
            Toast.makeText(getContext(), "Gallery app not found on device, please install one :)",
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForReadStorage(@NonNull PermissionRequest request) {
        logEventWithFromScreen(Events.Permission.READ_STORAGE_RATIONALE);
        new PermissionRationaleDialog(getString(R.string.permission_read_storage_rationale_title),
                getString(R.string.permission_read_storage_rationale_content), request).show(getActivity());
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showDeniedForReadStorage() {
        logEventWithFromScreen(Events.Permission.READ_STORAGE_DENY);
        new PermissionDenyDialog(getString(R.string.permission_read_storage_deny_title),
                getString(R.string.permission_read_storage_deny_content)).show(getActivity());
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showNeverAskForReadStorage() {
        logEventWithFromScreen(Events.Permission.READ_STORAGE_NEVER_ASK);
        new PermissionNeverAskDialog(getString(R.string.permission_read_storage_never_ask_title),
                getString(R.string.permission_read_storage_never_ask_content)).show(getActivity());
    }

    private void logEventWithFromScreen(@NonNull String eventName) {
        logEvent(new EventBuilder()
                .setEventName(eventName)
                .addParam(EventParamKeys.FROM_SCREEN, getScreenName())
                .build());
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
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    logSimpleEvent(Events.Register.REG_WITH_EMAIL_SELECT_IMAGE_CANCEL);
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
                logSimpleEvent(Events.Register.REG_WITH_EMAIL_SELECT_START_CROP);
                ProfilePictureCropper cropper = new ProfilePictureCropper(getContext());
                cropper.crop(RegisterSecondStepFragment.this, sourceUri, destinationUri);
            }

            @Override
            public void onCropped(@NonNull Uri croppedImage) {
                logSimpleEvent(Events.Register.REG_WITH_EMAIL_IMAGE_SELECTED);
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
        next(new Input(mCroppedPhoto, vDisplayNameInput.getText().toString()));
    }

    @NonNull
    private String getScreenName() {
        return EventValues.Screen.REGISTER_SECOND_STEP;
    }

    public class Input {
        @Nullable
        private final Uri mPhotoUri;
        @NonNull
        private final String mDisplayName;

        public Input(@Nullable Uri photoUri, @NonNull String displayName) {
            mPhotoUri = photoUri;
            mDisplayName = displayName;
        }

        @Nullable
        public Uri getPhotoUri() {
            return mPhotoUri;
        }

        @NonNull
        public String getDisplayName() {
            return mDisplayName;
        }
    }
}
