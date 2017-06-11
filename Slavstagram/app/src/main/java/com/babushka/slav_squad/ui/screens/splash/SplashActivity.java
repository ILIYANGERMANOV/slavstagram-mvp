package com.babushka.slav_squad.ui.screens.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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
        if (SessionManager.getInstance().isLoggedUser()) {
            MainActivity.startScreen(this);
        } else {
            LandingActivity.startScreen(this);
        }
    }
}
