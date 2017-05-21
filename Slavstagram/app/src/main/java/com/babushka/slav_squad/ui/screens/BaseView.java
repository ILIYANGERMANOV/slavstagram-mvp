package com.babushka.slav_squad.ui.screens;

import android.support.annotation.NonNull;

/**
 * Created by iliyan on 21.05.17.
 */

public interface BaseView<P extends BasePresenter> {
    void setPresenter(@NonNull P presenter);
}
