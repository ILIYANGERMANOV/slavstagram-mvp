package com.babushka.slav_squad.ui.screens.register.view;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.ui.screens.register.view.fragment.RegisterSecondStepFragment;
import com.babushka.slav_squad.ui.wizard.WizardSupport;

/**
 * Created by iliyan on 16.06.17.
 */

public interface RegisterSupport extends WizardSupport {
    void onFirstStepCompleted(@NonNull LoginDetails loginDetails);

    void onSecondStepCompleted(@NonNull RegisterSecondStepFragment.Input input);
}
