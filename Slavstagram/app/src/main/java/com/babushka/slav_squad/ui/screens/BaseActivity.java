package com.babushka.slav_squad.ui.screens;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by iliyan on 22.05.17.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;

    @LayoutRes
    protected abstract int getContentViewLayout();

    protected void onSetupUI() {
        //empty stub
    }

    @NonNull
    protected abstract P initializePresenter();

    protected void onSetupFinished() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());
        ButterKnife.bind(this);
        onSetupUI();
        mPresenter = initializePresenter();
        onSetupFinished();
    }
}
