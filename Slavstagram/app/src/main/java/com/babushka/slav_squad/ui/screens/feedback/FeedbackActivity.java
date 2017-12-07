package com.babushka.slav_squad.ui.screens.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.session.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by iliyan on 07.12.17.
 */

public class FeedbackActivity extends AppCompatActivity {

    @BindView(R.id.feedback_message_edit_text)
    AppCompatEditText vMessageInput;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setupActionBar();
    }

    @OnClick(R.id.feedback_send_button)
    public void onSend() {
        if (validateInput()) {
            String message = vMessageInput.getText().toString();
            Database.getInstance().saveFeedback(SessionManager.getInstance().getCurrentUser(), message);
            Toast.makeText(this, "Feedback sent. Thank you!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Cannot send empty feedback", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {
        return !TextUtils.isEmpty(vMessageInput.getText().toString().trim());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
