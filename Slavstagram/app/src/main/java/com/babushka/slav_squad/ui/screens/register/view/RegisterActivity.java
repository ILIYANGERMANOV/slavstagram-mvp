package com.babushka.slav_squad.ui.screens.register.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.ui.screens.register.RegisterContract;
import com.babushka.slav_squad.ui.screens.register.model.RegisterModel;
import com.babushka.slav_squad.ui.screens.register.presenter.RegisterPresenter;
import com.babushka.slav_squad.ui.screens.register.view.fragment.RegisterFirstStepFragment;
import com.babushka.slav_squad.ui.screens.register.view.fragment.RegisterSecondStepFragment;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.babushka.slav_squad.ui.wizard.WizardActionBarActivity;

/**
 * Created by iliyan on 13.06.17.
 */

public class RegisterActivity extends WizardActionBarActivity<RegisterContract.Presenter>
        implements RegisterContract.View, RegisterSupport {

    @Nullable
    private MaterialDialog mProgressDialog;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.register_fragment_container;
    }

    @NonNull
    @Override
    protected RegisterContract.Presenter initializePresenter() {
        return new RegisterPresenter(this, new RegisterModel());
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.startRegisterWizard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.getMusicPlayer().play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApp.getMusicPlayer().pause();
    }

    @Override
    public void showFirstStep() {
        changeFragment(RegisterFirstStepFragment.newInstance(), false);
    }

    @Override
    public void showSecondStep() {
        changeFragment(RegisterSecondStepFragment.newInstance(), true);
    }

    @Override
    public void setLoading() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title("Signing in")
                .content("Please wait...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @Override
    public void restartApp() {
        dismissProgressDialog();
        SplashActivity.startScreenAsEntryPoint(this);
    }

    @Override
    public void showError(@NonNull String message) {
        dismissProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onFirstStepCompleted(@NonNull LoginDetails loginDetails) {
        mPresenter.handleFirstStepCompleted(loginDetails);
    }

    @Override
    public void onSecondStepCompleted(@NonNull RegisterSecondStepFragment.Input input) {
        mPresenter.handleSecondStepCompleted(input);
    }

    @Nullable
    @Override
    protected String getScreenName() {
        return EventValues.Screen.REGISTER;
    }
}
