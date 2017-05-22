package com.babushka.slav_squad.ui.screens.login.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.screens.BaseActivity;
import com.babushka.slav_squad.ui.screens.login.LoginContract;
import com.babushka.slav_squad.ui.screens.login.presenter.LoginPresenter;
import com.babushka.slavstagram.R;

public class LoginActivity extends BaseActivity<LoginContract.Presenter>
        implements LoginContract.View {


    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onSetupUI() {

    }

    @NonNull
    @Override
    protected LoginContract.Presenter initializePresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onSetupFinished() {

    }
}
