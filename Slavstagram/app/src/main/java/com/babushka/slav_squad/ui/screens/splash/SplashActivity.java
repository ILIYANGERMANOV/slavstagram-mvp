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
import com.babushka.slav_squad.ui.screens.landing.landing.view.LandingActivity;
import com.babushka.slav_squad.ui.screens.main.view.MainActivity;
import com.babushka.slav_squad.util.Constants;

/**
 * Created by iliyan on 21.05.17.
 */

public class SplashActivity extends AppCompatActivity {
    public static void startScreenAsEntryPoint(@NonNull Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Constants.START_AS_ENTRY_POINT_FLAG);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        //TODO: remove ugly redirect delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirect();
            }
        }, 600);
    }

    private void setup() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void redirect() {
        if (SessionManager.getInstance().isLoggedUser()) {
            MainActivity.startScreen(this);
        } else {
            startActivity(new Intent(this, LandingActivity.class));
            overridePendingTransition(R.anim.slide_in_top, R.anim.stay);
        }
        finish();
    }
}
