package com.babushka.slav_squad.ui.screens.edit_profile.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.dialog.PermissionDenyDialog;
import com.babushka.slav_squad.ui.dialog.PermissionNeverAskDialog;
import com.babushka.slav_squad.ui.dialog.PermissionRationaleDialog;
import com.babushka.slav_squad.ui.listeners.editor.EditorGoListener;
import com.babushka.slav_squad.ui.screens.cropping.CropHandler;
import com.babushka.slav_squad.ui.screens.cropping.ProfilePictureCropper;
import com.babushka.slav_squad.ui.screens.edit_profile.EditProfileContract;
import com.babushka.slav_squad.ui.screens.edit_profile.model.EditProfileModel;
import com.babushka.slav_squad.ui.screens.edit_profile.presenter.EditProfilePresenter;
import com.babushka.slav_squad.util.IntentBuilder;
import com.google.gson.Gson;

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
 * Created by iliyan on 05.12.17.
 */
@RuntimePermissions
public class EditProfileActivity extends BaseActionBarActivity<EditProfilePresenter>
        implements EditProfileContract.View {
    private static final String EXTRA_USER = "user_extra";

    @BindView(R.id.edit_profile_change_photo_circle_image_view)
    CircleImageView vPhotoImage;
    @BindView(R.id.edit_profile_display_name_edit_text)
    EditText vDisplayNameInput;

    private User mUser;


    @Nullable
    private MaterialDialog mProgressDialog;

    public static void startScreenForResult(@NonNull Activity activity, @NonNull User user,
                                            int requestCode) {
        Intent intent = new Intent(activity, EditProfileActivity.class);
        intent.putExtra(EXTRA_USER, new Gson().toJson(user));
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mUser = new Gson().fromJson(intent.getStringExtra(EXTRA_USER), User.class);
        if (mUser == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vDisplayNameInput.setOnEditorActionListener(new EditorGoListener() {
            @Override
            protected boolean onAction() {
                saveChanges();
                return false;
            }
        });
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupUI();
    }

    @Override
    public void displayProfilePic(@NonNull String photoUrl) {
        GlideApp.with(this)
                .load(photoUrl)
                .dontAnimate()
                .into(vPhotoImage);
    }

    @Override
    public void displayName(@NonNull String displayName) {
        vDisplayNameInput.setText(displayName);
    }

    @Override
    public void openGalleryWithCheck(int requestCode) {
        EditProfileActivityPermissionsDispatcher.openGalleryWithCheck(this, requestCode);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void openGallery(int requestCode) {
        try {
            Intent intent = IntentBuilder.buildOpenGalleryWithChooserIntent(this);
            startActivityForResult(intent, requestCode);
        } catch (IntentBuilder.ResolveActivityException ignored) {
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
        ProfilePictureCropper cropper = new ProfilePictureCropper(this);
        cropper.crop(this, sourceUri, destinationUri);
    }

    @Override
    public void showSaveProgress() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title("Saving changes")
                .content("Please wait...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @Override
    public void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setResultOK() {
        setResult(RESULT_OK);
    }

    @Override
    public void closeScreen() {
        finish();
    }

    @Override
    public void showToast(@NonNull String message) {
        super.showToast(message);
    }

    @OnClick(R.id.edit_profile_change_photo_circle_image_view)
    public void onChangeProfilePic() {
        mPresenter.handleChangePhotoClick();
    }

    @OnClick(R.id.edit_profile_save_button)
    public void onSave() {
        saveChanges();
    }

    private void saveChanges() {
        if (validateInput()) {
            mPresenter.saveChanges(vDisplayNameInput.getText().toString());
        }
    }

    private boolean validateInput() {
        return !TextUtils.isEmpty(vDisplayNameInput.getText());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EditProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_edit_profile;
    }

    @NonNull
    @Override
    protected EditProfilePresenter initializePresenter() {
        return new EditProfilePresenter(this, new EditProfileModel(this),
                mUser, new CropHandler(this));
    }
}
