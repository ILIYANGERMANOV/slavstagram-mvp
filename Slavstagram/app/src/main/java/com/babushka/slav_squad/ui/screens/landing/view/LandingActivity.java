package com.babushka.slav_squad.ui.screens.landing.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.babushka.slav_squad.MusicPlayer;
import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.ui.BaseActivity;
import com.babushka.slav_squad.ui.custom_view.SlavSquadTermsAndCondsView;
import com.babushka.slav_squad.ui.custom_view.VolumeButton;
import com.babushka.slav_squad.ui.screens.landing.LandingContract;
import com.babushka.slav_squad.ui.screens.landing.LandingModelImpl;
import com.babushka.slav_squad.ui.screens.landing.presenter.LandingPresenter;
import com.babushka.slav_squad.ui.screens.login.view.LoginActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.OnClick;

public class LandingActivity extends BaseActivity<LandingContract.Presenter>
        implements LandingContract.View {

    //TODO: Refactor this ugly shit
    public static boolean sWasLanding = false;
    @BindView(R.id.landing_volume_button)
    VolumeButton vVolumeButton;
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
    @BindView(R.id.landing_slav_squad_terms_and_conds_view)
    SlavSquadTermsAndCondsView vTermsAndConds;
    @Nullable
    private MaterialDialog mProgressDialog;
    private boolean mIsLoading = false;

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
        MusicPlayer musicPlayer = MyApp.getMusicPlayer();
        return new LandingPresenter(this, new LandingModelImpl(),
                musicPlayer, getSimpleAnalytics());
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
        vVolumeButton.setFromScreen(getScreenName());
        vEmailButton.setPaintFlags(vEmailButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showSystemUI();
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showControlsWithAnimation();
            }
        }, 1500);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void showControlsWithAnimation() {
        ChainAnimation animation = new ChainAnimation.Builder(this)
                .add(vWelcomeText, R.anim.fade_in_slow)
                .add(vWelcomeCaptionText, R.anim.fade_in_slow)
                .add(vFbButton, R.anim.fade_in_fast)
                .add(vGoogleButotn, R.anim.fade_in_fast)
                .add(vDivider, R.anim.fade_in_fast)
                .add(vOrText, R.anim.fade_in_fast)
                .add(vEmailButton, R.anim.fade_in_fast)
                .add(vTermsAndConds, R.anim.fade_in_fast)
                .build();
        animation.run();
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupFacebookLogin(this, vInvisibleFbButton);
        vVolumeButton.volumeOff(false);
        sWasLanding = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vVolumeButton.isVolumeOn()) {
            mPresenter.playMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mIsLoading) {
            mPresenter.pauseMusic();
        }
    }

    @OnClick(R.id.landing_google_login_button)
    public void onLoginWithGoogleClick() {
        mPresenter.loginWithGoogle(this);
    }

    @OnClick(R.id.landing_fb_login_button)
    public void onLoginWithFbClick() {
        //TODO: Refatcor this internal call of showProgress(), it's work for the Presenter
        showProgress();
        logSimpleEvent(Events.Landing.LOGIN_WITH_FB);
        vInvisibleFbButton.callOnClick();
    }

    @OnClick(R.id.landing_email_button)
    public void onRegisterClick() {
        mPresenter.handleEmailClick();
    }

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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        mIsLoading = true;
        mProgressDialog = new MaterialDialog.Builder(this)
                .title("Logging in")
                .content("Will take a moment...")
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgress() {
        mIsLoading = false;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    protected String getScreenName() {
        return EventValues.Screen.LANDING;
    }
}
