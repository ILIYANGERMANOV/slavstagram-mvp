package com.blin1.slavstagram.ui.screens.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blin1.slavstagram.SessionManager;
import com.blin1.slavstagram.ui.screens.login.LoginActivity;
import com.blin1.slavstagram.ui.screens.main.MainActivity;

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
