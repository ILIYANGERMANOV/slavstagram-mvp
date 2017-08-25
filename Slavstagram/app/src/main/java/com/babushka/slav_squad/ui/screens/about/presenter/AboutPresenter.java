package com.babushka.slav_squad.ui.screens.about.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.about.AboutContract;
import com.babushka.slav_squad.ui.screens.about.AboutContract.Presenter;

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
}
