package com.babushka.slav_squad.ui.dialog;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.R;

/**
 * Created by iliyan on 20.08.17.
 */

public class PermissionDialog {

    @NonNull
    protected final String mTitle;
    @NonNull
    protected final String mContent;

    public PermissionDialog(@NonNull String title, @NonNull String content) {
        mTitle = title;
        mContent = content;
    }

    @CallSuper
    @NonNull
    protected MaterialDialog.Builder customizeDialog(@NonNull MaterialDialog.Builder builder, Activity activity) {
        return builder;
    }

    public void show(@NonNull Activity activity) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                .title(mTitle)
                .content(mContent)
                .positiveText(activity.getString(R.string.ok));
        customizeDialog(builder, activity);
        builder.show();
    }
}
