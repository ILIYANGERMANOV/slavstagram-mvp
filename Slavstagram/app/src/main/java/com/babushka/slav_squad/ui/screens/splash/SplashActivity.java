package com.babushka.slav_squad.ui.screens.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.babushka.slav_squad.SessionManager;
import com.babushka.slav_squad.ui.screens.login.view.LoginActivity;
import com.babushka.slav_squad.ui.screens.main.MainActivity;

/**
 * Created by iliyan on 21.05.17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SessionManager.getInstance().isLoggedUser()) {
            MainActivity.startScreen(this);
        } else {
            LoginActivity.startScreen(this);
        }
    }
}
