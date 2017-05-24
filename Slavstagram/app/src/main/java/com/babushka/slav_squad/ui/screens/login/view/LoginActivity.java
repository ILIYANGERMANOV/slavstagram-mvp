package com.babushka.slav_squad.ui.screens.login.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.babushka.slav_squad.session.UserDetails;
import com.babushka.slav_squad.ui.screens.BaseActivity;
import com.babushka.slav_squad.ui.screens.login.LoginContract;
import com.babushka.slav_squad.ui.screens.login.presenter.LoginPresenter;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.babushka.slavstagram.R;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.login_email_edit_text)
    EditText vEmailInput;
    @BindView(R.id.login_password_edit_text)
    EditText vPasswordInput;
    @BindView(R.id.login_fb_login_button)
    LoginButton vFBLoginButton;
    @BindView(R.id.login_google_sign_in_button)
    SignInButton vGoogleLoginButton;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_login;
    }

    @NonNull
    @Override
    protected LoginContract.Presenter initializePresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onSetupUI() {
        vGoogleLoginButton.setSize(SignInButton.SIZE_WIDE);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupFacebookLogin(this, vFBLoginButton);
    }

    @OnClick(R.id.login_google_sign_in_button)
    public void onLoginWithGooglePlusClicked() {
        mPresenter.loginWithGoogle(this);
    }

    @OnClick(R.id.login_login_button)
    public void onLoginClicked() {
        String email = vEmailInput.getText().toString();
        String password = vPasswordInput.getText().toString();
        UserDetails userDetails = new UserDetails(email, password);
        mPresenter.loginWithEmailAndPassword(this, userDetails);
    }

    @OnClick(R.id.login_register_button)
    public void onRegisterClicked() {
        mPresenter.handleRegisterClick();
    }

    @OnClick(R.id.login_skip_button)
    public void onSkipButtonClicked() {
        mPresenter.loginAsGuest();
    }

    @Override
    public void startRegisterScreen() {
        //TODO: Implement method
        Toast.makeText(this, "Register clicked (not implemented yet)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void restartApp() {
        SplashActivity.startScreenAsEntryPoint(this);
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
