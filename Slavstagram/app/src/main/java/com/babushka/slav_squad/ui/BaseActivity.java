package com.babushka.slav_squad.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.analytics.core.AnalyticsService;
import com.babushka.slav_squad.analytics.event.Event;
import com.babushka.slav_squad.analytics.event.EventBuilder;

import butterknife.ButterKnife;

/**
 * Created by iliyan on 22.05.17.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;

    @LayoutRes
    protected abstract int getContentViewLayout();

    protected void onReadArguments(@NonNull Intent intent) {
        //empty stub
    }

    protected void onSetupUI() {
        //empty stub

    }
    @NonNull
    protected abstract P initializePresenter();

    protected void onSetupFinished() {
        //empty stub
    }

    protected void onBeforeSetContentView() {
        //empty stub
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContentView();
        setContentView(getContentViewLayout());
        onReadArguments(getIntent());
        ButterKnife.bind(this);
        onSetupUI();
        mPresenter = initializePresenter();
        onSetupFinished();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }

    protected void showToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void logSimpleEvent(@NonNull String eventName) {
        Event event = EventBuilder.simpleEvent(eventName);
        getAnalytics().logEvent(event);
    }

    protected AnalyticsService getAnalytics() {
        return MyApp.getAnalytics();
    }

}
