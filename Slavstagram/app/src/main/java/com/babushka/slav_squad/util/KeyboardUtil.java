package com.babushka.slav_squad.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by iliyan on 11.06.17.
 */

public class KeyboardUtil {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder windowToken = null;
        if (activity.getCurrentFocus() != null) {
            windowToken = activity.getCurrentFocus().getWindowToken();
        }

        inputManager.hideSoftInputFromWindow(windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftKeyboardAndRequestFocus(@NonNull final Context context, @NonNull final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.requestFocus()) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }
        }, 150);
    }
}
