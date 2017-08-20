package com.babushka.slav_squad.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by iliyan on 20.08.17.
 */

public class PermissionNeverAskDialog extends PermissionDialog {
    public PermissionNeverAskDialog(@NonNull String title, @NonNull String content) {
        super(title, content);
    }

    @NonNull
    @Override
    protected MaterialDialog.Builder customizeDialog(@NonNull MaterialDialog.Builder builder, final Activity activity) {
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                openAppSettingsScreen(activity);
            }
        });
        return super.customizeDialog(builder, activity);
    }

    private void openAppSettingsScreen(@NonNull Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
