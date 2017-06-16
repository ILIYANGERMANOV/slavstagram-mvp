package com.babushka.slav_squad.ui.screens.landing.register.view.fragment;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.WizardFragment;
import com.babushka.slav_squad.ui.screens.landing.register.view.RegisterSupport;

/**
 * Created by iliyan on 16.06.17.
 */

public class RegisterSecondStepFragment extends WizardFragment<Uri, RegisterSupport> {
    public static RegisterSecondStepFragment newInstance() {
        return new RegisterSecondStepFragment();
    }

    @Override
    protected void onNext(@NonNull RegisterSupport support, Uri input) {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_second_step;
    }

    @Override
    protected void onInitialized() {

    }
}
