package com.babushka.slav_squad.ui.screens.about.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.about.AboutContract;
import com.babushka.slav_squad.ui.screens.about.presenter.AboutPresenter;
import com.babushka.slav_squad.ui.screens.terms_and_conditions.TermsAndConditionsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by iliyan on 25.08.17.
 */

public class AboutActivity extends BaseActionBarActivity<AboutContract.Presenter>
        implements AboutContract.View {

    @BindView(R.id.about_version_text_view)
    TextView vVersionText;
    @BindView(R.id.about_build_date_text_view)
    TextView vBuildDateText;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_about;
    }

    @NonNull
    @Override
    protected AboutContract.Presenter initializePresenter() {
        return new AboutPresenter(this);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupUI();
    }

    @Nullable
    @Override
    protected String getScreenName() {
        return EventValues.Screen.ABOUT;
    }

    @Override
    public void displayVersion(@NonNull String version) {
        vVersionText.setText(String.format("Version: %s", version));
    }

    @Override
    public void displayBuildDate(@NonNull Date buildDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
        vBuildDateText.setText(String.format("Build date: %s", dateFormat.format(buildDate)));
    }

    @OnClick(R.id.about_terms_and_conditions_button)
    public void onTermsAndCondsClick() {
        logSimpleEvent(Events.About.OPEN_TERMS_AND_CONDS);
        TermsAndConditionsActivity.startScreen(this);
    }
}
