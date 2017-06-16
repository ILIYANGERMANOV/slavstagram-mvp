package com.babushka.slav_squad.ui.screens.landing.register.view.fragment;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.session.UserDetails;
import com.babushka.slav_squad.ui.WizardFragment;
import com.babushka.slav_squad.ui.screens.landing.register.view.RegisterSupport;

/**
 * Created by iliyan on 16.06.17.
 */

public class RegisterFirstStepFragment extends WizardFragment<UserDetails, RegisterSupport> {
    public static RegisterFirstStepFragment newInstance() {
        return new RegisterFirstStepFragment();
    }

    @Override
    protected void onNext(@NonNull RegisterSupport support, @NonNull UserDetails input) {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_first_step;
    }

    @Override
    protected void onInitialized() {

    }
}
