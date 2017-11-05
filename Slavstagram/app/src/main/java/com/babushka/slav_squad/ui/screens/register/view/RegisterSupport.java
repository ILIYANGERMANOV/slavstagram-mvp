package com.babushka.slav_squad.ui.screens.register.view;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.session.data.UserDetails;
import com.babushka.slav_squad.ui.wizard.WizardSupport;

/**
 * Created by iliyan on 16.06.17.
 */

public interface RegisterSupport extends WizardSupport {
    void onUserDetailsEntered(@NonNull UserDetails userDetails);

    void onProfilePhotoSelected(@Nullable Uri photoUri);
}
