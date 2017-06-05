package com.babushka.slav_squad.util;

import android.os.Build;

/**
 * Created by iliyan on 05.06.17.
 */

public class AppUtil {
    public static boolean isPreKitkat() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLollipopOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
