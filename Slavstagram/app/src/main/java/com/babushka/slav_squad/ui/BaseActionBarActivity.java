package com.babushka.slav_squad.ui;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

/**
 * Created by iliyan on 07.06.17.
 */

public abstract class BaseActionBarActivity<P extends BasePresenter> extends BaseActivity<P> {
    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
