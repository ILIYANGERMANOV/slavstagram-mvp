package com.babushka.slav_squad.ui.screens.landing.register.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.landing.register.RegisterContract;
import com.babushka.slav_squad.ui.screens.landing.register.model.RegisterModel;
import com.babushka.slav_squad.ui.screens.landing.register.presenter.RegisterPresenter;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterActivity extends BaseActionBarActivity<RegisterContract.Presenter>
        implements RegisterContract.View, RegisterSupport {

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_register;
    }

    @NonNull
    @Override
    protected RegisterContract.Presenter initializePresenter() {
        return new RegisterPresenter(this, new RegisterModel());
    }
}
