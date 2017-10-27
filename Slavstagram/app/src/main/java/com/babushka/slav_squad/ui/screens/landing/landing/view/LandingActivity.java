package com.babushka.slav_squad.ui.screens.landing.landing.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.BaseActivity;
import com.babushka.slav_squad.ui.screens.landing.LandingModelImpl;
import com.babushka.slav_squad.ui.screens.landing.landing.LandingContract;
import com.babushka.slav_squad.ui.screens.landing.landing.presenter.LandingPresenter;
import com.babushka.slav_squad.ui.screens.landing.login.view.LoginActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.OnClick;

public class LandingActivity extends BaseActivity<LandingContract.Presenter>
        implements LandingContract.View {

    @BindView(R.id.landing_fb_login_invisible_button)
    LoginButton vInvisibleFbButton;
    @BindView(R.id.landing_welcome_text_view)
    TextView vWelcomeText;
    @BindView(R.id.landing_welcome_caption_text_view)
    TextView vWelcomeCaptionText;
    @BindView(R.id.landing_fb_login_button)
    Button vFbButton;
    @BindView(R.id.landing_google_login_button)
    Button vGoogleButotn;
    @BindView(R.id.landing_divider_view)
    View vDivider;
    @BindView(R.id.landing_or_text_view)
    TextView vOrText;
    @BindView(R.id.landing_email_button)
    Button vEmailButton;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, LandingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_landing;
    }

    @NonNull
    @Override
    protected LandingContract.Presenter initializePresenter() {
        return new LandingPresenter(this, new LandingModelImpl());
    }

    @Override
    protected void onBeforeSetContentView() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onSetupUI() {
        vEmailButton.setPaintFlags(vEmailButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showControlsWithAnimation();
            }
        }, 1500);
    }

    private void showControlsWithAnimation() {
        ChainAnimation animation = new ChainAnimation.Builder(this)
                .add(vWelcomeText, R.anim.fade_in)
                .add(vWelcomeCaptionText, R.anim.fade_in)
                .add(vFbButton, R.anim.fade_in)
                .add(vGoogleButotn, R.anim.fade_in)
                .add(vDivider, R.anim.fade_in)
                .add(vOrText, R.anim.fade_in)
                .add(vEmailButton, R.anim.fade_in)
                .build();
        animation.run();
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupFacebookLogin(this, vInvisibleFbButton);
    }

    @OnClick(R.id.landing_google_login_button)
    public void onLoginWithGoogleClicked() {
        mPresenter.loginWithGoogle(this);
    }

    @OnClick(R.id.landing_fb_login_button)
    public void onLoginWithFbClicked() {
        vInvisibleFbButton.callOnClick();
    }

    @OnClick(R.id.landing_email_button)
    public void onRegisterClicked() {
        mPresenter.handleEmailClick();
    }

//    @OnClick(R.id.landing_skip_button)
//    public void onSkipButtonClicked() {
//        mPresenter.loginAsGuest();
//    }

    @Override
    public void startLoginScreen() {
        LoginActivity.startScreen(this);
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
