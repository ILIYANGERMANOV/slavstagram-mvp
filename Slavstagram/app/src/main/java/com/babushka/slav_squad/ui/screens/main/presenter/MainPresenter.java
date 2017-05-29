package com.babushka.slav_squad.ui.screens.main.presenter;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.main.MainContract;

/**
 * Created by iliyan on 29.05.17.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenter(@NonNull MainContract.View view) {
        mView = view;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
