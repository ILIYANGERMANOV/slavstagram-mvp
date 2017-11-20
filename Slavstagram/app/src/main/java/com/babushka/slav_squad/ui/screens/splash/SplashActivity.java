package com.babushka.slav_squad.ui.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.special_start.SpecialStart;
import com.babushka.slav_squad.ui.screens.landing.view.LandingActivity;
import com.babushka.slav_squad.ui.screens.main.view.MainActivity;
import com.babushka.slav_squad.util.Constants;
import com.google.gson.Gson;

/**
 * Created by iliyan on 21.05.17.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String EXTRA_SPECIAL_START = "special_start_extra";

    public static void startScreenAsEntryPoint(@NonNull Context context) {
        Intent intent = buildEntryPointIntent(context, null);
        context.startActivity(intent);
    }

    @NonNull
    public static Intent buildEntryPointIntent(@NonNull Context context,
                                               @Nullable SpecialStart specialStart) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Constants.START_AS_ENTRY_POINT_FLAG);
        intent.putExtra(EXTRA_SPECIAL_START, new Gson().toJson(specialStart));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSpecialStartsIfAny();
        redirect();
    }

    private void loadSpecialStartsIfAny() {
        Intent intent = getIntent();
        String specialStartJson = intent.getStringExtra(EXTRA_SPECIAL_START);
        if (specialStartJson != null) {
            SpecialStart specialStart = new Gson().fromJson(specialStartJson, SpecialStart.class);
            SpecialStart.loadSpecialStart(this, specialStart);
        }
    }

    private void redirect() {
        if (SessionManager.getInstance().isLoggedUser()) {
            MainActivity.startScreen(this);
            finish();
        } else {
            setupForLanding();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startLandingScreen();
                }
            }, 600);
        }
    }

    private void setupForLanding() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void startLandingScreen() {
        startActivity(new Intent(this, LandingActivity.class));
        overridePendingTransition(R.anim.slide_in_top, R.anim.stay);
        finish();
    }
}
