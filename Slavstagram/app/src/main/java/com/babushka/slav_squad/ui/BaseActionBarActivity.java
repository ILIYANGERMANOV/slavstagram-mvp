package com.babushka.slav_squad.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.babushka.slav_squad.analytics.event.Events;

/**
 * Created by iliyan on 07.06.17.
 */

public abstract class BaseActionBarActivity<P extends BasePresenter> extends BaseActivity<P> {
    @CallSuper
    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setActionBarTitle(@NonNull String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @CallSuper
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            logHomeScreenEvent();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logHomeScreenEvent() {
        if (hasAnalytics()) {
            logSimpleEvent(Events.HOME_SCREEN_ + getScreenName());
        }
    }
}
