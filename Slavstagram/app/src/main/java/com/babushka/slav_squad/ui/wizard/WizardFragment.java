package com.babushka.slav_squad.ui.wizard;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.babushka.slav_squad.ui.BaseFragment;

/**
 * Created by iliyan on 16.06.17.
 */

public abstract class WizardFragment<T, S extends WizardSupport> extends BaseFragment {
    protected abstract void onNext(@NonNull S support, T input);

    @SuppressWarnings("unchecked")
    protected void next(T input) {
        FragmentActivity activity = getActivity();
        if (activity instanceof WizardSupport) {
            onNext(((S) activity), input);
        }
    }
}
