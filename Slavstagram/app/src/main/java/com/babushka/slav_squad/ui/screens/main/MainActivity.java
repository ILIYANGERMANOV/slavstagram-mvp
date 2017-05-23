package com.babushka.slav_squad.ui.screens.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.babushka.slavstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_info_text_view)
    TextView vInfoText;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
//        mTestTextView.setText(mTestTextView.getText() + " 100% working!");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            vInfoText.setText("Logged as: " + name);
        }
    }

    @OnClick(R.id.main_logout_button)
    public void logout() {
        SessionManager.getInstance().logout();
        SplashActivity.startScreenAsEntryPoint(this);
    }
}
