package com.babushka.slav_squad.ui.screens.terms_and_conditions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.TextView;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iliyan on 09.12.17.
 */

public class TermsAndConditionsActivity extends AppCompatActivity {

    @BindView(R.id.terms_and_conditions_content_text_view)
    TextView vContentText;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, TermsAndConditionsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);
        setupActionBar();
        displayTermsAndConds();
    }

    private void displayTermsAndConds() {
        String termsAndCondsString = AppUtil.readTextFromResource(this, R.raw.terms_and_conditions);
        Spanned termsAndConds = Html.fromHtml(termsAndCondsString);
        vContentText.setText(termsAndConds);
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
