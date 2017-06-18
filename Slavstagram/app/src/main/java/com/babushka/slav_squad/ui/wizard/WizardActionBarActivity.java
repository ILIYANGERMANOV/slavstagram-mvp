package com.babushka.slav_squad.ui.wizard;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.BasePresenter;

/**
 * Created by iliyan on 18.06.17.
 */

public abstract class WizardActionBarActivity<P extends BasePresenter>
        extends BaseActionBarActivity<P> {
    @IdRes
    protected abstract int getFragmentContainerId();

    protected void changeFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(getFragmentContainerId(), fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}
