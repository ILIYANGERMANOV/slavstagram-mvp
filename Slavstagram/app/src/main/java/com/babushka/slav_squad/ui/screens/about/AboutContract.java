package com.babushka.slav_squad.ui.screens.about;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.BasePresenter;

import java.util.Date;

/**
 * Created by iliyan on 25.08.17.
 */

public interface AboutContract {
    interface Presenter extends BasePresenter {
        void setupUI();
    }

    interface View {
        void displayVersion(@NonNull String version);

        void displayBuildDate(@NonNull Date buildDate);
    }
}
