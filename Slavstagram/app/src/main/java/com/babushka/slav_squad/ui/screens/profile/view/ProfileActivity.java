package com.babushka.slav_squad.ui.screens.profile.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.BaseActivity;
import com.babushka.slav_squad.ui.screens.profile.ProfileContract;
import com.babushka.slav_squad.ui.screens.profile.model.ProfileModel;
import com.babushka.slav_squad.ui.screens.profile.presenter.ProfilePresenter;

/**
 * Created by iliyan on 07.06.17.
 */

public class ProfileActivity extends BaseActivity<ProfileContract.Presenter>
        implements ProfileContract.View {

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_profile;
    }

    @NonNull
    @Override
    protected ProfileContract.Presenter initializePresenter() {
        return new ProfilePresenter(this, new ProfileModel());
    }
}
