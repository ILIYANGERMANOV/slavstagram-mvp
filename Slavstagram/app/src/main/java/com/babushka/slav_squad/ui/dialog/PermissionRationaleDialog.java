package com.babushka.slav_squad.ui.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import permissions.dispatcher.PermissionRequest;

/**
 * Created by iliyan on 20.08.17.
 */

public class PermissionRationaleDialog extends PermissionDialog {
    @NonNull
    private final PermissionRequest mRequest;

    public PermissionRationaleDialog(@NonNull String title, @NonNull String content,
                                     @NonNull PermissionRequest request) {
        super(title, content);
        mRequest = request;
    }

    @NonNull
    @Override
    protected MaterialDialog.Builder customizeDialog(@NonNull MaterialDialog.Builder builder, Activity activity) {
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                mRequest.proceed();
            }
        });
        return super.customizeDialog(builder, activity);
    }
}
