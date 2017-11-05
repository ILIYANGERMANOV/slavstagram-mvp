package com.babushka.slav_squad.ui.screens.register;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.session.data.UserDetails;
import com.babushka.slav_squad.ui.BasePresenter;
import com.babushka.slav_squad.ui.screens.landing.LandingModel;
import com.babushka.slav_squad.ui.screens.landing.LandingView;

/**
 * Created by iliyan on 13.06.17.
 */

public interface RegisterContract {
    interface View extends LandingView {
        void showFirstStep();

        void showSecondStep();

        void setLoading();

        void showError(@NonNull String message);
    }

    interface Presenter extends BasePresenter {
        void startRegisterWizard();

        void handleUserDetailsEntered(@NonNull UserDetails userDetails);

        void handleProfilePhotoSelected(@Nullable Uri photoPath);
    }

    interface Model extends LandingModel {
        @NonNull
        String getRandomProfileImageUrl();

        void uploadProfilePhoto(@NonNull Uri photoUri, @NonNull Storage.UploadImageListener listener);
    }
}
