package com.babushka.slav_squad.ui.screens.landing.login.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.listeners.editor.EditorGoListener;
import com.babushka.slav_squad.ui.screens.landing.LandingModelImpl;
import com.babushka.slav_squad.ui.screens.landing.login.LoginContract;
import com.babushka.slav_squad.ui.screens.landing.login.presenter.LoginPresenter;
import com.babushka.slav_squad.ui.screens.landing.register.view.RegisterActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by iliyan on 13.06.17.
 */

public class LoginActivity extends BaseActionBarActivity<LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.login_email_edit_text)
    EditText vEmailInput;
    @BindView(R.id.login_password_edit_text)
    EditText vPasswordInput;
    @BindView(R.id.login_login_button)
    Button vLoginButton;

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
        super.onSetupUI();
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPresenter != null) {
                    LoginDetails loginDetails = getUserDetailsFromInput();
                    mPresenter.handleInput(loginDetails);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        vEmailInput.addTextChangedListener(loginTextWatcher);
        vPasswordInput.addTextChangedListener(loginTextWatcher);
        vPasswordInput.setOnEditorActionListener(new EditorGoListener() {
            @Override
            protected boolean onAction() {
                LoginDetails loginDetails = getUserDetailsFromInput();
                if (mPresenter != null && mPresenter.isValidInput(loginDetails)) {
                    mPresenter.login(loginDetails);
                    return true;
                } else {
                    showToast("Invalid input!");
                }
                return false;
            }
        });
    }

    @OnClick(R.id.login_login_button)
    public void onLoginButtonClicked() {
        LoginDetails loginDetails = getUserDetailsFromInput();
        mPresenter.login(loginDetails);
    }

    @OnClick(R.id.login_register_button)
    public void onRegisterButtonClicked() {
        mPresenter.handleRegisterButtonClick();
    }

    @NonNull
    private LoginDetails getUserDetailsFromInput() {
        String email = vEmailInput.getText().toString();
        String password = vPasswordInput.getText().toString();
        return new LoginDetails(email, password);
    }

    @NonNull
    @Override
    protected LoginContract.Presenter initializePresenter() {
        return new LoginPresenter(this, new LandingModelImpl());
    }

    @Override
    public void restartApp() {
        SplashActivity.startScreenAsEntryPoint(this);
    }

    @Override
    public void enableLoginButton() {
        vLoginButton.setEnabled(true);
    }

    @Override
    public void disableLoginButton() {
        vLoginButton.setEnabled(false);
    }

    @Override
    public void startRegisterScreen() {
        RegisterActivity.startScreen(this);
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
