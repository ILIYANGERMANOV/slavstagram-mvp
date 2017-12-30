package com.babushka.slav_squad.ui.screens.about.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.BuildConfig;
import com.babushka.slav_squad.ui.screens.about.AboutContract;
import com.babushka.slav_squad.ui.screens.about.AboutContract.Presenter;

import java.util.Date;

/**
 * Created by iliyan on 25.08.17.
 */

public class AboutPresenter implements Presenter {
    private AboutContract.View mView;

    public AboutPresenter(@NonNull AboutContract.View view) {
        mView = view;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void setupUI() {
        mView.displayVersion(BuildConfig.VERSION_NAME);
        mView.displayBuildDate(new Date(BuildConfig.BUILD_TIMESTAMP));
    }
}
